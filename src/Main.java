import model.Expense;
import service.ExpenseManager;
import service.SummaryTask;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE = "expenses.csv";
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager(FILE);
        try { manager.load(); } catch (Exception e) { System.err.println("Load: " + e.getMessage()); }

        SummaryTask st = new SummaryTask(manager);
        Thread t = new Thread(st);
        t.setDaemon(true);
        t.start();

        Scanner sc = new Scanner(System.in); boolean exit=false;
        while (!exit) {
            printMenu();
            String cmd = sc.nextLine().trim();
            try {
                switch (cmd) {
                    case "1":
                        int id = manager.nextId();
                        System.out.print("Category: "); String cat = sc.nextLine();
                        System.out.print("Desc: "); String desc = sc.nextLine();
                        System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine());
                        System.out.print("Date (yyyy-mm-dd) or empty: "); String d = sc.nextLine();
                        LocalDate date = d.isEmpty() ? LocalDate.now() : LocalDate.parse(d);
                        manager.add(new Expense(id, cat, desc, amt, date));
                        System.out.println("Added.");
                        break;
                    case "2":
                        List<Expense> all = manager.listAll();
                        all.forEach(e -> System.out.println(e));
                        break;
                    case "3":
                        System.out.print("Category: "); String c = sc.nextLine();
                        manager.filterByCategory(c).forEach(System.out::println);
                        break;
                    case "4":
                        manager.totalByCategory().forEach((k,v)-> System.out.println(k + " => " + v));
                        break;
                    case "5":
                        manager.save();
                        System.out.println("Saved.");
                        break;
                    case "0":
                        exit = true; break;
                    default: System.out.println("unknown");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        st.stop();
        try { manager.save(); } catch (Exception ignored) {}
        System.out.println("Bye.");
    }

    private static void printMenu() {
        System.out.println("\nExpense Tracker");
        System.out.println("1) Add expense");
        System.out.println("2) List all");
        System.out.println("3) Filter by category");
        System.out.println("4) Report totals by category");
        System.out.println("5) Save now");
        System.out.println("0) Exit");
        System.out.print("> ");
    }
}
