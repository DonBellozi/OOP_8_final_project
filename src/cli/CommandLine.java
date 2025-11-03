package cli;

import model.*;
import service.UserService;
import service.ReportService;
import util.Validator;
import java.util.*;

public class CommandLine {
    private final Scanner sc = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final ReportService reportService = new ReportService();
    private User current;

    public void start() {
        System.out.println("=== Система управления личными финансами ===");
        while (true) {
            if (current == null) authMenu();
            else mainMenu();
        }
    }

    private void authMenu() {
        System.out.println("\n1 - Войти");
        System.out.println("2 - Зарегистрироваться");
        System.out.println("3 - Выход");
        System.out.print("> ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> login();
            case "2" -> register();
            case "3" -> System.exit(0);
            default -> System.out.println("Некорректный выбор, попробуйте снова.");
        }
    }

    private void mainMenu() {
        System.out.println("\n--- Главное меню (" + current.login + ") ---");
        System.out.println("""
                1 - Добавить доход
                2 - Добавить расход
                3 - Установить бюджет
                4 - Показать общие доходы, расходы и баланс
                5 - Показать бюджеты и остатки
                6 - Подсчёт по категориям
                7 - Экспорт в CSV
                8 - Сохранить и выйти
                """);
        System.out.print("> ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> addIncome();
            case "2" -> addExpense();
            case "3" -> setBudget();
            case "4" -> current.wallet.printSummary();
            case "5" -> current.wallet.printBudgets();
            case "6" -> countByCategories();
            case "7" -> exportCSV();
            case "8" -> logout();
            default -> System.out.println("Некорректный ввод, выберите пункт 1–8.");
        }
    }

    private void login() {
        System.out.print("Логин: ");
        String l = sc.nextLine();
        System.out.print("Пароль: ");
        String p = sc.nextLine();
        current = userService.login(l, p);
        if (current != null)
            System.out.println("Добро пожаловать, " + l + "!");
        else
            System.out.println("Неверный логин или пароль.");
    }

    private void register() {
        System.out.print("Введите логин: ");
        String l = sc.nextLine();
        System.out.print("Введите пароль: ");
        String p = sc.nextLine();
        if (userService.register(l, p))
            System.out.println("Регистрация успешна!");
        else
            System.out.println("Пользователь уже существует.");
    }

    private void addIncome() {
        System.out.print("Категория дохода: ");
        String cat = sc.nextLine();
        double sum = Validator.readPositive(sc, "Сумма: ");
        System.out.print("Комментарий: ");
        String note = sc.nextLine();
        current.wallet.addIncome(cat, sum, note);
    }

    private void addExpense() {
        System.out.print("Категория расхода: ");
        String cat = sc.nextLine();
        double sum = Validator.readPositive(sc, "Сумма: ");
        System.out.print("Комментарий: ");
        String note = sc.nextLine();
        current.wallet.addExpense(cat, sum, note);
    }

    private void setBudget() {
        System.out.print("Категория: ");
        String cat = sc.nextLine();
        double limit = Validator.readPositive(sc, "Лимит бюджета: ");
        current.wallet.setBudget(cat, limit);
        System.out.println("Бюджет установлен.");
    }

    private void countByCategories() {
        System.out.print("Введите категории через запятую: ");
        String[] parts = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim).filter(x -> !x.isEmpty()).toArray(String[]::new);

        double inc = current.wallet.sumByCategories(Arrays.asList(parts), Transaction.Type.INCOME);
        double exp = current.wallet.sumByCategories(Arrays.asList(parts), Transaction.Type.EXPENSE);
        System.out.printf("Суммарно по [%s]: доход %.2f | расход %.2f%n",
                String.join(", ", parts), inc, exp);
    }

    private void exportCSV() {
        String path = reportService.exportToCSV(current);
        System.out.println("Отчёт сохранён в " + path);
    }

    private void logout() {
        current.wallet.save(current.login);
        current = null;
        System.out.println("Данные сохранены. Выход выполнен.");
    }
}
