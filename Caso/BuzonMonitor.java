package Caso;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonMonitor<T> {
    private final Queue<T> cola = new LinkedList<>();
    private final int capacidad;

    public BuzonMonitor(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void put(T item) {
        // TODO: Implementa espera cuando el buzon este lleno (si capacidad > 0).
        // TODO: Inserta el item y notifica a consumidores.
        throw new UnsupportedOperationException("TODO: implementar put");
    }

    public synchronized T take() {
        // TODO: Implementa espera cuando el buzon este vacio.
        // TODO: Retira un item y notifica a productores.
        throw new UnsupportedOperationException("TODO: implementar take");
    }

    public synchronized int size() {
        return cola.size();
    }
}
