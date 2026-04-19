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
        // TODO: Consumir eventos del buzon del servidor.
        // TODO: Si evento FIN -> terminar.
        // TODO: Si es normal -> simular procesamiento entre 100 y 1000 ms.
    }
}
