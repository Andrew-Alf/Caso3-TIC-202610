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

        BuzonMonitor<Evento> buzonEntrada = new BuzonMonitor<>(0);
        BuzonMonitor<Evento> buzonAlertas = new BuzonMonitor<>(0);
        BuzonMonitor<Evento> buzonClasificacion = new BuzonMonitor<>(config.tam1);

        List<BuzonMonitor<Evento>> buzonesServidores = new ArrayList<>();
        for (int i = 0; i < config.ns; i++) {
            buzonesServidores.add(new BuzonMonitor<>(config.tam2));
        }

        AtomicLong secuencia = new AtomicLong(0);
        AtomicInteger clasificadoresTerminados = new AtomicInteger(0);
        List<Thread> hilos = new ArrayList<>();

        // TODO 1: Crear hilos de sensores (1..ni), cada uno con eventosPorSensor(sensorId).
        // TODO 2: Crear hilo BrokerAnalizador con totalEventosEsperados().
        // TODO 3: Crear hilo Administrador.
        // TODO 4: Crear hilos de clasificadores (nc) compartiendo clasificadoresTerminados y nc.
        // TODO 5: Crear hilos de servidores (ns), uno por buzon.
        // TODO 6: Iniciar todos los hilos.
        // TODO 7: Hacer join a todos y verificar buzones vacios al final.

        System.out.println("Plantilla lista. Completa los TODOs de MainCaso y actores.");
    }
}
