package service;

import model.Expense;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private final File file;

    public ExpenseManager(String filename) { this.file = new File(filename); }

    public synchronized void add(Expense e) { expenses.add(e); }
    public synchronized boolean removeById(int id) { return expenses.removeIf(e -> e.getId() == id); }
    public synchronized List<Expense> listAll() { return new ArrayList<>(expenses); }
    public synchronized int nextId() { return expenses.stream().mapToInt(Expense::getId).max().orElse(0)+1; }

    public synchronized void save() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Expense e : expenses) bw.write(e.toString() + "\n");
        }
    }
    public synchronized void load() throws IOException {
        expenses.clear();
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null && !l.isEmpty()) expenses.add(Expense.fromCsv(l));
        }
    }

    public synchronized Map<String, Double> totalByCategory() {
        return expenses.stream().collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));
    }

    public synchronized double total() { return expenses.stream().mapToDouble(Expense::getAmount).sum(); }

    public synchronized List<Expense> filterByCategory(String cat) {
        return expenses.stream().filter(e -> e.getCategory().equalsIgnoreCase(cat)).collect(Collectors.toList());
    }
}
