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
