package model;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type {INCOME, EXPENSE}

    public final Type type;
    public final String category;
    public final double amount;
    public final String note;
    public final LocalDateTime createdAt;

    public Transaction(Type type, String category, double amount, String note) {
        this(type, category, amount, note, LocalDateTime.now());
    }

    public Transaction(Type type, String category, double amount, String note, LocalDateTime createdAt) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note == null ? "" : note;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("%s | %.2f | %s | %s", type, amount, category, note);
    }
}
