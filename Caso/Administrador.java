package Caso;
//a
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
        while (true) {
            Evento alerta = buzonAlertas.take();
            if (alerta == null) {
                break;
            }

            if (alerta.esFin()) {
                for (int i = 0; i < cantidadClasificadores; i++) {
                    buzonClasificacion.put(Evento.fin());
                }
                System.out.println("[Administrador] Recibio FIN y envio "
                    + cantidadClasificadores + " FIN a clasificadores.");
                break;
            }

            int r = random.nextInt(21);
            if (r % 4 == 0) {
                Evento normal = alerta.comoNormal();
                buzonClasificacion.put(normal);
                System.out.println("[Administrador] Alerta liberada " + normal + " (r=" + r + ")");
            } else {
                System.out.println("[Administrador] Alerta descartada " + alerta + " (r=" + r + ")");
            }
        }

        System.out.println("[Administrador] Termino.");
    }
}
