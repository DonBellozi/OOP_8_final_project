import model.User;
import model.Wallet;
import model.Transaction;
import org.junit.jupiter.api.Test;
import service.WalletService;

import static org.junit.jupiter.api.Assertions.*;

public class WalletServiceTest {

    @Test
    void addIncomeAndExpenseThroughService() {
        String login = "wstest_" + System.nanoTime();
        User user = new User(login, "pwd");
        user.wallet = new Wallet(); // чистый кошелёк

        WalletService ws = new WalletService();
        ws.addIncome(user, "Зарплата", 10000.0, "");
        ws.addExpense(user, "Еда", 1500.0, "");

        assertEquals(10000.0, user.wallet.totalIncome(), 0.0001);
        assertEquals(1500.0, user.wallet.totalExpense(), 0.0001);
        assertEquals(8500.0,
                user.wallet.totalIncome() - user.wallet.totalExpense(),
                0.0001);
    }

    @Test
    void setBudgetViaService() {
        String login = "wstest2_" + System.nanoTime();
        User user = new User(login, "pwd");
        user.wallet = new Wallet();

        WalletService ws = new WalletService();
        ws.setBudget(user, "Еда", 3000.0);
        ws.addExpense(user, "Еда", 1000.0, "");

        assertTrue(user.wallet.budgets.containsKey("Еда"));
        double spent = user.wallet.sumByCategory("Еда", Transaction.Type.EXPENSE);
        assertEquals(1000.0, spent, 0.0001);
    }
}
