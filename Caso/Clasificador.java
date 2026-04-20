package Caso;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Clasificador implements Runnable {
    private final int id;
    private final BuzonMonitor<Evento> buzonClasificacion;
    private final List<BuzonMonitor<Evento>> buzonesServidores;
    private final AtomicInteger clasificadoresTerminados;
    private final int totalClasificadores;

    public Clasificador(int id,
                        BuzonMonitor<Evento> buzonClasificacion,
                        List<BuzonMonitor<Evento>> buzonesServidores,
                        AtomicInteger clasificadoresTerminados,
                        int totalClasificadores) {
        this.id = id;
        this.buzonClasificacion = buzonClasificacion;
        this.buzonesServidores = buzonesServidores;
        this.clasificadoresTerminados = clasificadoresTerminados;
        this.totalClasificadores = totalClasificadores;
    }

    @Override
    public void run() {
        while (true) {
            Evento evento;
            try {
                evento = buzonClasificacion.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Clasificador " + id + "] Interrumpido esperando evento.");
                break;
            }

            if (evento == null) break;

            if (evento.esFin()) {
                int actual = clasificadoresTerminados.incrementAndGet();
                System.out.println("[Clasificador " + id + "] Recibio FIN ("
                        + actual + "/" + totalClasificadores + ")");

                if (actual == totalClasificadores) {
                    for (BuzonMonitor<Evento> buzonServidor : buzonesServidores) {
                        try {
                            buzonServidor.put(Evento.fin());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("[Clasificador " + id
                                    + "] Interrumpido enviando FIN a servidores.");
                            break;
                        }
                    }
                    System.out.println("[Clasificador " + id
                            + "] Fue el ultimo y envio FIN a servidores.");
                }
                break;
            }

            int destino = evento.getDestinoServidor();
            if (destino < 0 || destino >= buzonesServidores.size()) {
                destino = Math.floorMod(destino, buzonesServidores.size());
            }

            try {
                buzonesServidores.get(destino).put(evento);
                System.out.println("[Clasificador " + id + "] Envio " + evento
                        + " a servidor " + destino);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Clasificador " + id + "] Interrumpido enviando a servidor.");
                break;
            }
        }

        System.out.println("[Clasificador " + id + "] Termino.");
    }
}