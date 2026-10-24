package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.config.JdbcConfig;
import school.sptech.dao.EmbarcadoDAO;
import school.sptech.dao.EmpresaDAO;
import school.sptech.dto.captura.CapturaDTO;
import school.sptech.dto.captura.ComponenteDTO;
import school.sptech.model.Embarcado;
import school.sptech.model.Empresa;
import school.sptech.service.EmpresaService;
import school.sptech.service.JiraService;
import school.sptech.service.S3Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String bucketName = "flowtech-datalake";

        JdbcTemplate connection = JdbcConfig.getJdbcTemplate();
        S3Service s3Service = new S3Service();
        JiraService jiraService = new JiraService();

        EmbarcadoDAO embarcadoDAO = new EmbarcadoDAO(connection);

        EmpresaDAO empresaDAO = new EmpresaDAO(connection);
        EmpresaService empresaService = new EmpresaService(empresaDAO);

        List<CapturaDTO> capturasDeHoje = s3Service.lerCapturasHoraAtual(bucketName);
        if(capturasDeHoje.isEmpty()) {
            int hora = LocalDateTime.now().getHour();
            System.out.printf("[ AVISO ] Nenhuma Captura encontrada em: %s às %02dhr(s)\n", bucketName, hora);
            return;
        }

        Map<String, List<CapturaDTO>> capturasPorMac = agruparCapturasPorEnderecoMac(capturasDeHoje);
        capturasPorMac.forEach((enderecoMac, capturas) -> {
            String situacaoEmbarcado = verificarSituacaoEmbarcado(capturas);
            if(situacaoEmbarcado.equalsIgnoreCase("Normal")) return;

            String macFormatado = formatarMac(enderecoMac);
            Embarcado embarcado = embarcadoDAO.getByEnderecoMac(macFormatado);
            if(embarcado == null) return;

            Empresa empresa = empresaDAO.getById(embarcado.getEmpresaId());
            if(empresa == null || !empresaService.credenciaisJiraValidas(empresa)) return;


            List<String> componentesMonitorados = new ArrayList<>();
            for (CapturaDTO captura : capturas) {
                String componenteNome = captura.nome().toUpperCase();
                componentesMonitorados.add(componenteNome);
            }

            String timestampFormatado = capturas.getFirst().timestamp().split(" ")[1];

            String summary = "%s %s | %s às %s".formatted(
                    situacaoEmbarcado.equalsIgnoreCase("critica") ? "🔴" : "🟡",
                    embarcado.getModelo(),
                    String.join(", ", componentesMonitorados),
                    timestampFormatado);
            String descricao = gerarDescricaoJira(embarcado ,capturas, empresa);
            String issueType = "Alerta";

            try {
                String response = jiraService.createIssue(
                        empresa.getJiraBaseUrl(),
                        empresa.getJiraEmail(),
                        empresa.getJiraApiKey(),
                        empresa.getJiraProjectKey(),
                        summary,
                        descricao,
                        issueType
                );
                System.out.printf("[ LOG ] Issue criada para a Empresa %s: %s\n", empresa.getRazaoSocial(), response);
            } catch (Exception e) {
                System.err.println("[ ERRO ] Falha ao abrir Issue no Jira: " + e.getMessage());
            }
        });
    }

    private static Map<String, List<CapturaDTO>> agruparCapturasPorEnderecoMac(List<CapturaDTO> capturasDeHoje) {
        Map<String, List<CapturaDTO>> capturasPorMac = new HashMap<>();
        for (CapturaDTO captura : capturasDeHoje) {
            String enderecoMacAtual = captura.enderecoMac();

            Boolean novoValor =
                    capturasPorMac.putIfAbsent(enderecoMacAtual, new ArrayList<>(List.of(captura))) == null;

            if(!novoValor) {
                List<CapturaDTO> capturas = capturasPorMac.get(enderecoMacAtual);
                capturas.add(captura);
                capturasPorMac.replace(enderecoMacAtual, capturas);
            }
        }
        return capturasPorMac;
    }

    private static String verificarSituacaoEmbarcado(List<CapturaDTO> capturas) {
        // Regra de Negócio:
        // - Se ao menos 1 métrica for Crítica -> Embarcado em estado CRÍTICO 🔴
        // - Se ao menos 1 métrica for Alerta -> Embarcado em estado ALERTA 🟡

        String situacao = "Normal";
        for (CapturaDTO captura : capturas) {
            for (ComponenteDTO parametro : captura.parametros()) {
                if(parametro.situacao().equalsIgnoreCase("Crítico")) {
                    situacao = "Crítico";
                    break;
                };

                if(parametro.situacao().equalsIgnoreCase("Alerta")) situacao = "Alerta";
            }
        }

        return situacao;
    }


    private static String gerarDescricaoJira(Embarcado embarcado, List<CapturaDTO> capturas, Empresa empresa) {
        StringBuilder sb = new StringBuilder();

        sb.append("[🚨] RELATÓRIO DE CAPTURA\n");
        sb.append("Data/Hora da Captura: ").append(capturas.getFirst().timestamp()).append("\n");
        sb.append("Empresa: ").append(empresa.getNomeFantasia()).append("\n\n");

        sb.append("[ EMBARCADO - INFORMAÇÕES ]\n");
        sb.append("• Apelido/Hostname: ").append(embarcado.getApelido()).append("\n");
        sb.append("• Endereço MAC: ").append(formatarMac(embarcado.getEnderecoMac())).append("\n");
        sb.append("• Modelo: ").append(embarcado.getModelo()).append("\n");
        sb.append("• ID: ").append(embarcado.getIdEmbarcado()).append("\n\n");

        sb.append("[ MÉTRICAS FORA DO PADRÃO ]\n");
        for (CapturaDTO captura : capturas) {
            String componenteNome = captura.nome().toUpperCase();

            for (ComponenteDTO parametro : captura.parametros()) {
                if(!parametro.situacao().equalsIgnoreCase("Normal")) {
                    String icone = parametro.situacao().equalsIgnoreCase("Crítico") ? "🔴" : "🟡";

                    String parametroNome = parametro.nome().split("_")[0];
                    String unidadeMedida = parametro.nome().split("_")[1];
                    if(unidadeMedida.equals("pct")) unidadeMedida = "%";

                    String limiteMax =
                            parametro.limiteMax() != null ? "%.2f".formatted(parametro.limiteMax()) : "NA";
                    String limiteMin =
                            parametro.limiteMin() != null ? "%.2f".formatted(parametro.limiteMin()) : "NA";

                    sb.append("%s %s -> %s: ".formatted(icone, componenteNome, parametroNome));
                    sb.append("Última Leitura: ").append(parametro.valor()).append(" ").append(unidadeMedida);
                    sb.append(" (Limites: Máx ").append(limiteMax).append(" | Min ").append(limiteMin).append(")\n");
                }
            }
        }

        return sb.toString();
    }

    private static String formatarMac(String enderecoMac) throws IllegalArgumentException {
        if (enderecoMac == null) {
            return null;
        }

        String macFormatado = enderecoMac.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (macFormatado.length() != 12) {
            throw new IllegalArgumentException("Endereço MAC inválido. Deve conter 12 caracteres.");
        }

        // Insere os dois-pontos a cada 2 caracteres usando Regex
        return macFormatado.replaceAll("(.{2})(?=.)", "$1:");
    }

}
