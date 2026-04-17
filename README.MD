# Productor-Consumidor usando el Patrón Monitor

Este ejemplo muestra cómo el **patrón Monitor** permite coordinar la ejecución concurrente entre un **productor** y un **consumidor** que comparten un recurso común. El monitor garantiza que **solo un hilo accede al recurso compartido a la vez**, bloqueando a los demás cuando sea necesario.

---

## 🧩 Descripción del problema

Se desea simular un sistema donde un **productor** genera elementos (por ejemplo, números) y los coloca en un **buffer compartido**. Un **consumidor** toma los elementos del buffer para procesarlos.

En este ejemplo, el buffer puede contener **solo un elemento** (`counter = 0` → vacío, `counter = 1` → lleno). Si el consumidor intenta consumir cuando el buffer está vacío, **debe esperar** hasta que el productor coloque un nuevo elemento. Asimismo, el productor no puede insertar un nuevo elemento si el buffer ya está lleno.

---

## 🧱 Estructura de archivos

* `SharedResourceMonitor.java` → Implementa el monitor que controla el acceso al recurso compartido.
* `Producer.java` → Clase que representa al productor.
* `Consumer.java` → Clase que representa al consumidor.
* `Main.java` → Contiene el método `main()` y crea los hilos.

---

## 🧠 Funcionamiento del monitor

El **monitor** se implementa utilizando métodos `synchronized`, junto con las primitivas `wait()` y `notifyAll()`.

* `wait()` bloquea el hilo actual hasta que otro hilo invoque `notifyAll()`.
* `notifyAll()` despierta a todos los hilos que están esperando en el monitor.
* Solo un hilo puede ejecutar un método `synchronized` del monitor a la vez.

De este modo:

* Si el consumidor llama a `consume()` y el buffer está vacío, el hilo entra en **espera pasiva**.
* Cuando el productor agrega un elemento con `produce()`, invoca `notifyAll()` para **despertar** al consumidor.

---

## 🧮 Código

### `SharedResourceMonitor.java`

```java
public class SharedResourceMonitor {
    private int item;
    private int counter = 0; // 0 = empty, 1 = full

    public synchronized void produce(int value) {
        while (counter == 1) {
            try {
                System.out.println("[Monitor] Buffer full, producer waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        item = value;
        counter = 1;
        System.out.println("[Monitor] Producer produced: " + value);
        notifyAll(); // Wake up waiting consumers
    }

    public synchronized int consume() {
        while (counter == 0) {
            try {
                System.out.println("[Monitor] Buffer empty, consumer waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        counter = 0;
        System.out.println("[Monitor] Consumer consumed: " + item);
        notifyAll(); // Wake up waiting producers
        return item;
    }
}
```

---

### `Producer.java`

```java
import java.util.Scanner;

public class Producer implements Runnable {
    private SharedResourceMonitor monitor;
    private Scanner scanner = new Scanner(System.in);

    public Producer(SharedResourceMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            System.out.print("[Producer] Enter a number to produce (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("[Producer] Exiting...");
                System.exit(0);
            }
            try {
                int value = Integer.parseInt(input);
                monitor.produce(value);
            } catch (NumberFormatException e) {
                System.out.println("[Producer] Invalid input, please enter an integer.");
            }
        }
    }
}
```

---

### `Consumer.java`

```java
public class Consumer implements Runnable {
    private SharedResourceMonitor monitor;

    public Consumer(SharedResourceMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            int value = monitor.consume();
            System.out.println("[Consumer] Processed value: " + value);
            try {
                Thread.sleep(2000); // Simulate processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

---

### `Main.java`

```java
public class Main {
    public static void main(String[] args) {
        SharedResourceMonitor monitor = new SharedResourceMonitor();

        Thread producerThread = new Thread(new Producer(monitor));
        Thread consumerThread = new Thread(new Consumer(monitor));

        producerThread.start();
        consumerThread.start();
    }
}
```

---

## 🔍 Explicación paso a paso

1. **El productor** pide al usuario ingresar un número y lo intenta colocar en el buffer.

   * Si el buffer está lleno (`counter == 1`), se bloquea hasta que el consumidor consuma un elemento.

2. **El consumidor** intenta consumir un elemento.

   * Si el buffer está vacío (`counter == 0`), el consumidor espera hasta que el productor coloque un nuevo valor.

3. **El monitor (`SharedResourceMonitor`)** garantiza exclusión mutua:

   * Solo un hilo (productor o consumidor) puede ejecutar sus métodos sincronizados al mismo tiempo.
   * Cuando uno está dentro, el otro debe esperar fuera del monitor.

4. **Finalización:**

   * Si el usuario escribe `exit`, el programa finaliza de forma controlada.
