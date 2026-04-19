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
        // TODO: Genera 'cantidadEventos' eventos.
        // Pista: id global, codigo en [1..numeroServidores], destinoServidor segun codigo.
        // TODO: Deposita cada evento en buzonEntrada.
        // TODO: Registra en consola cuando termine el sensor.
    }
}
