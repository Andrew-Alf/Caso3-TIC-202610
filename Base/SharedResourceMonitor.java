package Base;
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
