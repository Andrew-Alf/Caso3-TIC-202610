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
        // TODO: Procesa exactamente 'totalEsperado' eventos del buzon de entrada.
        // Regla: genera r en [0..200]. Si r % 8 == 0 -> ALERTA; si no -> NORMAL.
        // TODO: Envia cada evento al buzon correspondiente.
        // TODO: Al terminar, envia un FIN al administrador por buzonAlertas.
    }
}
