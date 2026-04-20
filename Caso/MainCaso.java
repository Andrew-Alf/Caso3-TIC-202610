package Caso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MainCaso {
    public static void main(String[] args) {
        String rutaConfig = args.length > 0 ? args[0] : "Caso/config.txt";
        LectorConfiguracionArchivo config;

        try {
            config = LectorConfiguracionArchivo.desdeArchivo(rutaConfig);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la configuracion: " + e.getMessage());
            System.err.println("Uso: java Caso.MainCaso <ruta-config>");
            return;
        }

        // --- Buzones ---
        BuzonMonitor<Evento> buzonEntrada = new BuzonMonitor<>(-1);
        BuzonMonitor<Evento> buzonAlertas = new BuzonMonitor<>(-1);
        BuzonMonitor<Evento> buzonClasificacion = new BuzonMonitor<>(config.tam1);

        List<BuzonMonitor<Evento>> buzonesServidores = new ArrayList<>();
        for (int i = 0; i < config.ns; i++) {
            buzonesServidores.add(new BuzonMonitor<>(config.tam2));
        }

        // --- Contadores compartidos ---
        AtomicLong secuencia = new AtomicLong(0);
        AtomicInteger clasificadoresTerminados = new AtomicInteger(0);
        List<Thread> hilos = new ArrayList<>();

        // --- TODO 1: Sensores ---
        int totalEventos = 0;
        for (int i = 1; i <= config.ni; i++) {
            int eventosEsteSensor = config.baseEventos * i;
            totalEventos += eventosEsteSensor;
            Sensor sensor = new Sensor(i, eventosEsteSensor, config.ns, buzonEntrada, secuencia);
            hilos.add(new Thread(sensor, "Sensor-" + i));
        }

        // --- TODO 2: Broker ---
        BrokerAnalizador broker = new BrokerAnalizador(
                buzonEntrada, buzonAlertas, buzonClasificacion, totalEventos);
        hilos.add(new Thread(broker, "Broker"));

        // --- TODO 3: Administrador ---
        Administrador admin = new Administrador(
                buzonAlertas, buzonClasificacion, config.nc);
        hilos.add(new Thread(admin, "Administrador"));

        // --- TODO 4: Clasificadores ---
        for (int i = 0; i < config.nc; i++) {
            Clasificador clasificador = new Clasificador(
                    i, buzonClasificacion, buzonesServidores,
                    clasificadoresTerminados, config.nc);
            hilos.add(new Thread(clasificador, "Clasificador-" + i));
        }

        // --- TODO 5: Servidores ---
        for (int i = 0; i < config.ns; i++) {
            ServidorConsolidacion servidor = new ServidorConsolidacion(i, buzonesServidores.get(i));
            hilos.add(new Thread(servidor, "Servidor-" + i));
        }

        // --- TODO 6: Iniciar todos ---
        System.out.println("=== Iniciando sistema IoT ===");
        System.out.println("Sensores: " + config.ni + " | Total eventos: " + totalEventos);
        System.out.println("Clasificadores: " + config.nc + " | Servidores: " + config.ns);

        long tiempoInicio = System.currentTimeMillis();
        for (Thread h : hilos) {
            h.start();
        }

        // --- TODO 7: Join y verificacion final ---
        for (Thread h : hilos) {
            try {
                h.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main interrumpido esperando " + h.getName());
            }
        }

        long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
        System.out.println("\n=== Sistema terminado en " + tiempoTotal + "ms ===");

        verificarBuzon("buzonEntrada", buzonEntrada);
        verificarBuzon("buzonAlertas", buzonAlertas);
        verificarBuzon("buzonClasificacion", buzonClasificacion);
        for (int i = 0; i < config.ns; i++) {
            verificarBuzon("buzonServidor-" + i, buzonesServidores.get(i));
        }
    }

    private static void verificarBuzon(String nombre, BuzonMonitor<?> buzon) {
        int size = buzon.size();
        String estado = size == 0 ? "OK" : "ERROR - quedan " + size + " eventos";
        System.out.println("  [" + estado + "] " + nombre);
    }
}