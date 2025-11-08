package service;

import model.Transaction;
import model.User;

import java.util.Arrays;
import java.util.List;

public class WalletService {

    public void addIncome(User user, String category, double amount, String note) {
        user.wallet.addIncome(category, amount, note);
    }

    public void addExpense(User user, String category, double amount, String note) {
        user.wallet.addExpense(category, amount, note);
    }

    public void setBudget(User user, String category, double limit) {
        user.wallet.setBudget(category, limit);
    }

    public void printSummary(User user) {
        user.wallet.printSummary();
    }

    public void printBudgets(User user) {
        user.wallet.printBudgets();
    }

    public void countByCategories(User user, String[] categories) {
        List<String> list = Arrays.asList(categories);
        double inc = user.wallet.sumByCategories(list, Transaction.Type.INCOME);
        double exp = user.wallet.sumByCategories(list, Transaction.Type.EXPENSE);
        System.out.printf("Суммарно по [%s]: доход %.2f | расход %.2f%n",
                String.join(", ", list), inc, exp);
    }
}
