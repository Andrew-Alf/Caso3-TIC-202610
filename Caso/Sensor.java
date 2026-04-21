package Caso;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Sensor implements Runnable {
    private final int sensorId;
    private final int cantidadEventos;
    private final int numeroServidores;
    private final BuzonMonitor<Evento> buzonEntrada;
    private final AtomicLong secuenciaGlobal;
    private final Random random;

    public Sensor(int sensorId, int cantidadEventos, int numeroServidores,
                  BuzonMonitor<Evento> buzonEntrada, AtomicLong secuenciaGlobal) {
        this.sensorId = sensorId;
        this.cantidadEventos = cantidadEventos;
        this.numeroServidores = numeroServidores;
        this.buzonEntrada = buzonEntrada;
        this.secuenciaGlobal = secuenciaGlobal;
        this.random = new Random(sensorId * 31L + System.nanoTime());
    }

    @Override
    public void run() {
        for (int i = 0; i < cantidadEventos; i++) {
            long id = secuenciaGlobal.incrementAndGet();
            int codigo = random.nextInt(numeroServidores) + 1;
            int destinoServidor = codigo - 1;

            Evento evento = Evento.normal(id, sensorId, codigo, destinoServidor);
            try {
                buzonEntrada.put(evento);
                System.out.println("[Sensor " + sensorId + "] Genero " + evento);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Sensor " + sensorId + "] Interrumpido durante produccion.");
                break;
            }
        }

        System.out.println("[Sensor " + sensorId + "] Termino. Eventos generados: " + cantidadEventos);
    }
}
