package Caso;

import java.util.Random;

public class ServidorConsolidacion implements Runnable {
    private final int id;
    private final BuzonMonitor<Evento> buzon;
    private final Random random = new Random();

    public ServidorConsolidacion(int id, BuzonMonitor<Evento> buzon) {
        this.id = id;
        this.buzon = buzon;
    }

    @Override
    public void run() {
        System.out.println("[Servidor " + id + "] Iniciado.");

        while (true) {
            try {
                Evento evento = buzon.take();

                if (evento.esFin()) {
                    System.out.println("[Servidor " + id + "] Recibio evento de fin. Terminando.");
                    break;
                }

                int tiempoProcesamiento = 100 + random.nextInt(901);
                System.out.println("[Servidor " + id + "] Procesando " + evento +
                        " durante " + tiempoProcesamiento + "ms.");
                Thread.sleep(tiempoProcesamiento);

                System.out.println("[Servidor " + id + "] Termino de procesar " + evento);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Servidor " + id + "] Interrumpido.");
                break;
            }
        }

        System.out.println("[Servidor " + id + "] Buzón final size: " + buzon.size());
    }
}
