package Caso;

import java.util.Random;

public class BrokerAnalizador implements Runnable {
    private final BuzonMonitor<Evento> buzonEntrada;
    private final BuzonMonitor<Evento> buzonAlertas;
    private final BuzonMonitor<Evento> buzonClasificacion;
    private final int totalEsperado;
    private final Random random = new Random();

    public BrokerAnalizador(BuzonMonitor<Evento> buzonEntrada,
                            BuzonMonitor<Evento> buzonAlertas,
                            BuzonMonitor<Evento> buzonClasificacion,
                            int totalEsperado) {
        this.buzonEntrada = buzonEntrada;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
        this.totalEsperado = totalEsperado;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalEsperado; i++) {
            Evento evento;
            try {
                evento = buzonEntrada.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            int r = random.nextInt(201);
            if (r % 8 == 0) {
                Evento alerta = Evento.alerta(
                    evento.getId(),
                    evento.getSensorId(),
                    evento.getCategoria(),
                    evento.getDestinoServidor()
                );
                try {
                    buzonAlertas.put(alerta);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                System.out.println("[Broker] ALERTA " + alerta + " (r=" + r + ")");
            } else {
                Evento normal = evento.comoNormal();
                try {
                    buzonClasificacion.put(normal);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                System.out.println("[Broker] NORMAL " + normal + " (r=" + r + ")");
            }
        }

        try {
            buzonAlertas.put(Evento.fin());
            System.out.println("[Broker] Termino y envio FIN al administrador.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
