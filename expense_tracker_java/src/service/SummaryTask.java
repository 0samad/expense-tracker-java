package service;

public class SummaryTask implements Runnable {
    private final ExpenseManager manager;
    private volatile boolean running = true;

    public SummaryTask(ExpenseManager manager) { this.manager = manager; }

    public void stop() { running = false; }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(15000);
                System.out.println("[Summary] Total expenses: " + manager.total());
                System.out.println("[Summary] By category: " + manager.totalByCategory());
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
        }
    }
}
