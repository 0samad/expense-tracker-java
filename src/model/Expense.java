package model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private String category;
    private String description;
    private double amount;
    private LocalDate date;

    public Expense(int id, String category, String description, double amount, LocalDate date) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
    public int getId() { return id; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return id + "," + category + "," + description.replace(",", " ") + "," + amount + "," + date.toString();
    }

    public static Expense fromCsv(String line) {
        String[] p = line.split(",",5);
        return new Expense(Integer.parseInt(p[0]), p[1], p[2], Double.parseDouble(p[3]), LocalDate.parse(p[4]));
    }
}
