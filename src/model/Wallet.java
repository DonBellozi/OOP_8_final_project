package model;

import java.io.*;
import java.util.*;

public class Wallet {
    public final List<Transaction> transactions = new ArrayList<>();
    public final Map<String, Double> budgets = new HashMap<>();

    public void addIncome(String cat, double sum, String note) {
        transactions.add(new Transaction(Transaction.Type.INCOME, cat, sum, note));
        checkTotals();
    }

    public void addExpense(String cat, double sum, String note) {
        transactions.add(new Transaction(Transaction.Type.EXPENSE, cat, sum, note));
        checkBudget(cat);
        checkTotals();
    }

    public void setBudget(String cat, double limit) {
        budgets.put(cat, limit);
    }

    public double totalIncome() {
        return sum(t -> t.type == Transaction.Type.INCOME);
    }

    public double totalExpense() {
        return sum(t -> t.type == Transaction.Type.EXPENSE);
    }

    public double sumByCategory(String cat, Transaction.Type type) {
        return sum(t -> t.type == type && t.category.equalsIgnoreCase(cat));
    }

    public double sumByCategories(Collection<String> cats, Transaction.Type type) {
        Set<String> normalized = new HashSet<>();
        for (String c : cats)
            normalized.add(c.trim().toLowerCase(Locale.ROOT));

        return sum(t ->
                t.type == type && normalized.contains(t.category.toLowerCase(Locale.ROOT))
        );
    }

    private double sum(java.util.function.Predicate<Transaction> filter) {
        return transactions.stream()
                .filter(filter)
                .mapToDouble(t -> t.amount)
                .sum();
    }

    public void printSummary() {
        double income = totalIncome();
        double expense = totalExpense();
        System.out.printf("Общий доход: %.2f%n", income);
        System.out.printf("Общие расходы: %.2f%n", expense);
        System.out.printf("Баланс: %.2f%n", income - expense);
    }

    public void printBudgets() {
        if (budgets.isEmpty()) {
            System.out.println("Бюджеты не заданы.");
            return;
        }
        System.out.println("\nБюджеты по категориям:");
        for (var e : budgets.entrySet()) {
            double spent = sumByCategory(e.getKey(), Transaction.Type.EXPENSE);
            double remaining = e.getValue() - spent;
            System.out.printf("- %s: лимит %.2f | потрачено %.2f | остаток %.2f%n",
                    e.getKey(), e.getValue(), spent, remaining);
        }
    }

    private void checkBudget(String cat) {
        if (!budgets.containsKey(cat)) return;
        double limit = budgets.get(cat);
        double spent = sumByCategory(cat, Transaction.Type.EXPENSE);

        if (spent > limit)
            System.out.printf("!!! Превышен лимит по '%s': %.2f / %.2f%n", cat, spent, limit);
        else if (spent >= 0.8 * limit)
            System.out.printf("!!! Потрачено более 80%% бюджета по '%s'%n", cat);
    }

    private void checkTotals() {
        if (totalExpense() > totalIncome())
            System.out.println("!!! Внимание: расходы превышают доходы!");
    }

    public void save(String login) {
        try (PrintWriter pw = new PrintWriter(login + "_wallet.txt")) {
            for (var e : budgets.entrySet())
                pw.println("B;" + e.getKey() + ";" + e.getValue());

            for (var t : transactions)
                pw.printf("T;%s;%s;%.2f;%s;%s%n",
                        t.type, t.category, t.amount, t.note, t.createdAt);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public static Wallet load(String login) {
        Wallet w = new Wallet();
        File f = new File(login + "_wallet.txt");
        if (!f.exists()) return w;

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("B;")) {
                    String[] p = line.split(";");
                    if (p.length >= 3)
                        w.budgets.put(p[1], Double.parseDouble(p[2]));
                } else if (line.startsWith("T;")) {
                    String[] p = line.split(";");
                    if (p.length >= 5) {
                        Transaction.Type type = Transaction.Type.valueOf(p[1]);
                        String cat = p[2];
                        double amount = Double.parseDouble(p[3]);
                        String note = p[4];
                        w.transactions.add(new Transaction(type, cat, amount, note));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
        return w;
    }
}
