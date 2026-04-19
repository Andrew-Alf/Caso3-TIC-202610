package Base;
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
