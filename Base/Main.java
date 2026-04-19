package Base;
public class Main {
    public static void main(String[] args) {
        SharedResourceMonitor monitor = new SharedResourceMonitor();

        Thread producerThread = new Thread(new Producer(monitor));
        Thread consumerThread = new Thread(new Consumer(monitor));

        producerThread.start();
        consumerThread.start();
    }
}
