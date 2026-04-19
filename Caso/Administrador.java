package Caso;

import java.util.Random;

public class Administrador implements Runnable {
    private final BuzonMonitor<Evento> buzonAlertas;
    private final BuzonMonitor<Evento> buzonClasificacion;
    private final int cantidadClasificadores;
    private final Random random = new Random();

    public Administrador(BuzonMonitor<Evento> buzonAlertas,
                         BuzonMonitor<Evento> buzonClasificacion,
                         int cantidadClasificadores) {
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
        this.cantidadClasificadores = cantidadClasificadores;
    }

    @Override
    public void run() {
        // TODO: Consumir alertas hasta recibir FIN.
        // Regla: genera r en [0..20]. Si r % 4 == 0 -> evento normal a clasificacion; si no, descartar.
        // TODO: Cuando llegue FIN, enviar 'cantidadClasificadores' eventos FIN a clasificacion.
    }
}
