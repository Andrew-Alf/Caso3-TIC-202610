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
            Evento evento = buzonEntrada.take();
            if (evento == null) {
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
                buzonAlertas.put(alerta);
                System.out.println("[Broker] ALERTA " + alerta + " (r=" + r + ")");
            } else {
                Evento normal = evento.comoNormal();
                buzonClasificacion.put(normal);
                System.out.println("[Broker] NORMAL " + normal + " (r=" + r + ")");
            }
        }

        buzonAlertas.put(Evento.fin());
        System.out.println("[Broker] Termino y envio FIN al administrador.");
    }
}
