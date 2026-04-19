package Caso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectorConfiguracionArchivo {
    public final int ni;
    public final int baseEventos;
    public final int nc;
    public final int ns;
    public final int tam1;
    public final int tam2;

    public LectorConfiguracionArchivo(int ni, int baseEventos, int nc, int ns, int tam1, int tam2) {
        this.ni = ni;
        this.baseEventos = baseEventos;
        this.nc = nc;
        this.ns = ns;
        this.tam1 = tam1;
        this.tam2 = tam2;
        validar();
    }

    private void validar() {
        if (ni <= 0 || baseEventos <= 0 || nc <= 0 || ns <= 0 || tam1 <= 0 || tam2 <= 0) {
            throw new IllegalArgumentException("Todos los parametros deben ser mayores a 0.");
        }
    }

    public int eventosPorSensor(int sensorId) {
        return baseEventos * sensorId;
    }

    public int totalEventosEsperados() {
        int suma = 0;
        for (int i = 1; i <= ni; i++) {
            suma += eventosPorSensor(i);
        }
        return suma;
    }

    public static LectorConfiguracionArchivo desdeArchivo(String ruta) throws IOException {
        List<String> lineas = Files.readAllLines(Path.of(ruta));

        Map<String, Integer> kv = new HashMap<>();
        List<Integer> secuenciales = new ArrayList<>();

        for (String linea : lineas) {
            String limpia = linea.trim();
            if (limpia.isEmpty() || limpia.startsWith("#")) {
                continue;
            }

            if (limpia.contains("=")) {
                String[] partes = limpia.split("=", 2);
                String clave = partes[0].trim().toLowerCase();
                int valor = Integer.parseInt(partes[1].trim());
                kv.put(clave, valor);
            } else {
                secuenciales.add(Integer.parseInt(limpia));
            }
        }

        if (!kv.isEmpty()) {
            int ni = leerClave(kv, "ni", "sensores");
            int base = leerClave(kv, "baseeventos", "base", "eventosbase");
            int nc = leerClave(kv, "nc", "clasificadores");
            int ns = leerClave(kv, "ns", "servidores");
            int tam1 = leerClave(kv, "tam1", "capclasificacion");
            int tam2 = leerClave(kv, "tam2", "capconsolidacion");
            return new LectorConfiguracionArchivo(ni, base, nc, ns, tam1, tam2);
        }

        if (secuenciales.size() < 6) {
            throw new IllegalArgumentException("Se esperaban 6 valores en el archivo de configuracion.");
        }

        return new LectorConfiguracionArchivo(
            secuenciales.get(0),
            secuenciales.get(1),
            secuenciales.get(2),
            secuenciales.get(3),
            secuenciales.get(4),
            secuenciales.get(5)
        );
    }

    private static int leerClave(Map<String, Integer> kv, String... claves) {
        for (String clave : claves) {
            if (kv.containsKey(clave)) {
                return kv.get(clave);
            }
        }
        throw new IllegalArgumentException("Falta clave de configuracion: " + String.join("/", claves));
    }
}
