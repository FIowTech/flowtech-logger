package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.config.JdbcConfig;
import school.sptech.dao.EmpresaDAO;
import school.sptech.dto.captura.CapturaDTO;
import school.sptech.dto.captura.ComponenteDTO;
import school.sptech.dto.captura.MetricaDTO;
import school.sptech.model.Empresa;
import school.sptech.service.EmpresaService;
import school.sptech.service.JiraService;
import school.sptech.service.S3Service;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String bucketName = "flowtech-datalake";

        JdbcTemplate connection = JdbcConfig.getJdbcTemplate();
        S3Service s3Service = new S3Service();
        JiraService jiraService = new JiraService();

        EmpresaDAO empresaDAO = new EmpresaDAO(connection);
        EmpresaService empresaService = new EmpresaService(empresaDAO);

        List<CapturaDTO> capturasDeHoje = s3Service.lerCapturasHoraAtual(bucketName);
        if(capturasDeHoje.isEmpty()) {
            int hora = LocalDateTime.now().getHour();
            System.out.printf("[ AVISO ] Nenhuma Captura encontrada em: %s às %02dhr(s)\n", bucketName, hora);
            return;
        }

        for (CapturaDTO captura : capturasDeHoje) {
            String situacaoEmbarcado = verificarSituacaoEmbarcado(captura);
            if(situacaoEmbarcado.equalsIgnoreCase("controlada")) continue;

            Empresa empresa = empresaService.buscarEmpresaEValidarCredenciaisJira(captura.idEmpresa());
            if(empresa == null) continue;

            String summary = "%s %s | %s às %s".formatted(
                    situacaoEmbarcado.equalsIgnoreCase("critica") ? "🔴" : "🟡",
                    captura.modelo(),
                    captura.componentes().keySet(),
                    captura.timestamp().split(" ")[1]);
            String descricao = gerarDescricaoJira(captura, empresa.getRazaoSocial());
            String issueType = "Bug";

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
        }
    }

    private static String verificarSituacaoEmbarcado(CapturaDTO captura) {
        // Regra de Negócio:
        // - Se ao menos 1 métrica for Crítica -> Embarcado em estado CRÍTICO 🔴
        // - Se ao menos 1 métrica for Atenção -> Embarcado em estado ATENÇÃO 🟡
        String situacao = "controlada";
        if(captura.componentes() == null) return situacao;

        List<ComponenteDTO> componentes = captura.componentes().values().stream().toList();
        for (ComponenteDTO c : componentes) {
            List<MetricaDTO> metricas = c.metricas().values().stream().toList();
            for (MetricaDTO m : metricas) {
                if(m.situacao().equalsIgnoreCase("critica")) {
                    situacao = "critica";
                    break;
                };
                if(m.situacao().equalsIgnoreCase("atencao")) {
                    situacao = "atencao";
                }
            }
        }

        return situacao;
    }

    private static String gerarDescricaoJira(CapturaDTO captura, String empresaName) {
        StringBuilder sb = new StringBuilder();

        sb.append("[🚨] RELATÓRIO DE CAPTURA\n");
        sb.append("Data/Hora da Captura: ").append(captura.timestamp()).append("\n");
        sb.append("Empresa: ").append(empresaName).append("\n\n");

        sb.append("[ EMBARCADO - INFORMAÇÕES ]\n");
        sb.append("• Apelido/Hostname: ").append(captura.apelido()).append("\n");
        sb.append("• Endereço MAC: ").append(captura.enderecoMac()).append("\n");
        sb.append("• Modelo: ").append(captura.modelo()).append("\n");
        sb.append("• ID: ").append(captura.idEmbarcado()).append("\n\n");

        sb.append("[ MÉTRICAS FORA DO PADRÃO ]\n");

        captura.componentes().forEach((cName, c) -> {
            if (c.metricas() != null) {
                c.metricas().forEach((mName, m) -> {
                    if (m.situacao() != null && !m.situacao().equalsIgnoreCase("controlada")) {
                        String icone = m.situacao().equalsIgnoreCase("critica") ? "🔴" : "🟡";

                        String unidadeDeMedida = m.unidadeMedida() != null ?
                                m.unidadeMedida().equalsIgnoreCase("pct") ?
                                "%" : m.unidadeMedida() : "";

                        String limiteMin = m.limiteMin() != null ? m.limiteMin().toString() : "NA";
                        String limiteMax = m.limiteMax() != null ? m.limiteMax().toString() : "NA";

                        sb.append("%s %s -> %s: ".formatted(icone, cName.toUpperCase(), mName));
                        sb.append("Última Leitura: ").append(m.valor()).append(" ").append(unidadeDeMedida);
                        sb.append(" (Limites: Min ").append(limiteMin).append(" | Máx ").append(limiteMax).append(")\n");
                    }
                });
            }
        });

        return sb.toString();
    }
}