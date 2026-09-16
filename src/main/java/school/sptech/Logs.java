package school.sptech;

import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

public class Logs {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            Double leituraCPU = ThreadLocalRandom.current().nextDouble(5, 101);
            String mensagemCPU = (leituraCPU > 80) ? "Crítico: Uso de CPU crítico sobre o limite permitido." :
                    (leituraCPU > 55) ? "Atenção: Uso de CPU em alerta sobre o limite permitido." :
                            "Conforme.";
            Double leituraRAM = ThreadLocalRandom.current().nextDouble(30, 101);
            String mensagemRAM = (leituraRAM > 85) ? "Crítico: Uso de RAM crítico sobre o limite permitido." :
                    (leituraRAM > 65) ? "Atenção: Uso de RAM em alerta sobre o limite permitido." :
                            "Conforme.";
            Double leituraDisco = ThreadLocalRandom.current().nextDouble(20, 101);
            String mensagemDisco = (leituraDisco > 85) ? "Crítico: Uso de Disco crítico sobre o limite permitido." :
                    (leituraDisco > 70) ? "Atenção: Uso de Disco em alerta sobre o limite permitido." :
                            "Conforme.";
            Double leituraNet = ThreadLocalRandom.current().nextDouble(0, 15);
            String mensagemNet = (leituraNet > 8) ? "Crítico: Uso de Rede crítico sobre o limite permitido." :
                    (leituraNet > 4) ? "Atenção: Uso de Rede em alerta sobre o limite permitido." :
                            (leituraNet < 0.2) ? "Atenção: Uso de Rede em alerta sobre o limite permitido." :
                                    "Conforme.";
            System.out.println("Iniciando leitura...");
            System.out.printf("%s | Leitura CPU: %.2f%% | %s%n", ZonedDateTime.now(), leituraCPU, mensagemCPU);
            System.out.printf("%s | Leitura RAM: %.2f%% | %s%n", ZonedDateTime.now(), leituraRAM, mensagemRAM);
            System.out.printf("%s | Leitura Disco: %.2f%% | %s%n", ZonedDateTime.now(), leituraDisco, mensagemDisco);
            System.out.printf("%s | Leitura Rede: %.2f Mbps | %s%n", ZonedDateTime.now(), leituraNet, mensagemNet);
            System.out.println("Leitura finalizada...");
        }, 0, 2, TimeUnit.SECONDS);
    }
}








