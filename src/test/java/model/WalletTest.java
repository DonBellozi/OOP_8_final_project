import model.Transaction;
import model.Wallet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    void incomeExpenseTotals() {
        Wallet w = new Wallet();
        w.addIncome("Зарплата", 20000, "");
        w.addIncome("Бонус", 3000, "");
        w.addExpense("Еда", 500, "");
        w.addExpense("Еда", 300, "");
        assertEquals(23000.0, w.totalIncome(), 0.0001);
        assertEquals(800.0, w.totalExpense(), 0.0001);
    }

    @Test
    void sumByCategories() {
        Wallet w = new Wallet();
        w.addExpense("Еда", 1000, "");
        w.addExpense("Такси", 500, "");
        double total = w.sumByCategories(
                List.of("Еда", "Такси"),
                Transaction.Type.EXPENSE
        );
        assertEquals(1500.0, total, 0.0001);
    }

    @Test
    void budgets() {
        Wallet w = new Wallet();
        w.setBudget("Еда", 4000);
        w.addExpense("Еда", 3200, "");
        assertTrue(w.budgets.containsKey("Еда"));
        double spent = w.sumByCategory("Еда", Transaction.Type.EXPENSE);
        assertEquals(3200.0, spent, 0.0001);
    }
}
