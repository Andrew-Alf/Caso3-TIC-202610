package Caso;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonMonitor<T> {
    private final Queue<T> cola = new LinkedList<>();
    private final int capacidad; 

    public BuzonMonitor(int capacidad) {
        this.capacidad = capacidad;
    }

    //Aquí importante en el momento de hacer las pruebas poner capacidad -1 para simular que es ilimitada 
    public synchronized void put(T item) throws InterruptedException {
        while (capacidad > 0 && cola.size() >= capacidad) {
            wait();
        }
        cola.add(item);
        //importante aqui notifyAll y no notify para evitar un posible bloqueo infinito
        notifyAll(); 
    }

    public synchronized T take() throws InterruptedException {
        while (cola.isEmpty()) {
            System.out.println("[Monitor] Buffer empty, consumer waiting...");
            wait();
        }
        T item = cola.poll();
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return cola.size();
    }
}