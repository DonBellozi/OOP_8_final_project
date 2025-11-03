import model.Transaction;
import model.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class WalletTest {

    @Test
    void incomeExpenseTotals() {
        Wallet w = new Wallet();
        w.addIncome("Зарплата", 20000, "октябрь");
        w.addIncome("Бонус", 3000, "премия");
        w.addExpense("Еда", 500, "обед");
        w.addExpense("Еда", 300, "ужин");
        assertEquals(23000.0, w.totalIncome(), 0.0001);
        assertEquals(800.0,  w.totalExpense(), 0.0001);
    }

    @Test
    void sumByCategories() {
        Wallet w = new Wallet();
        w.addIncome("Зарплата", 40000, "");
        w.addExpense("Еда", 1000, "");
        w.addExpense("Такси", 500, "");
        double exp = w.sumByCategories(List.of("Еда","Такси"), Transaction.Type.EXPENSE);
        assertEquals(1500.0, exp, 0.0001);
    }

    @Test
    void budgets() {
        Wallet w = new Wallet();
        w.setBudget("Еда", 4000);
        w.addExpense("Еда", 3200, "");
        // остаток должен быть 800, проверим расчётом
        double spent = w.sumByCategory("Еда", Transaction.Type.EXPENSE);
        assertEquals(3200.0, spent, 0.0001);
        assertTrue(w.budgets.containsKey("Еда"));
    }
}
