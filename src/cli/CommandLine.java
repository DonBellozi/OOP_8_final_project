package cli;

import model.Transaction;
import model.User;
import service.UserService;
import service.ReportService;
import service.WalletService;
import util.Validator;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLine {

    private final Scanner sc = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final ReportService reportService = new ReportService();
    private final WalletService walletService = new WalletService();

    private User current;

    public void start() {
        System.out.println("=== Система управления личными финансами ===");
        while (true) {
            if (current == null) {
                authMenu();
            } else {
                mainMenu();
            }
        }
    }

    // ---------- Меню авторизации ----------

    private void authMenu() {
        System.out.println("\n1 - Войти");
        System.out.println("2 - Зарегистрироваться");
        System.out.println("3 - Выход");
        System.out.print("> ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> login();
            case "2" -> register();
            case "3" -> {
                System.out.println("Выход из приложения.");
                System.exit(0);
            }
            default -> System.out.println("Некорректный выбор, попробуйте снова.");
        }
    }

    private void login() {
        System.out.print("Логин: ");
        String l = sc.nextLine();
        System.out.print("Пароль: ");
        String p = sc.nextLine();
        current = userService.login(l, p);
        if (current != null) {
            System.out.println("Добро пожаловать, " + l + "!");
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }

    private void register() {
        System.out.print("Введите логин: ");
        String l = sc.nextLine();
        System.out.print("Введите пароль: ");
        String p = sc.nextLine();
        if (userService.register(l, p)) {
            System.out.println("Регистрация успешна! Теперь можно войти.");
        } else {
            System.out.println("Пользователь с таким логином уже существует.");
        }
    }

    // ---------- Главное меню ----------

    private void mainMenu() {
        System.out.println("\n--- Главное меню (" + current.login + ") ---");
        System.out.println("""
                1 - Добавить доход
                2 - Добавить расход
                3 - Установить бюджет на категорию
                4 - Показать общие доходы, расходы и баланс
                5 - Показать бюджеты и остатки по категориям
                6 - Подсчёт по выбранным категориям
                7 - Экспорт отчёта в CSV
                8 - Сохранить и выйти из аккаунта
                """);
        System.out.print("> ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> addIncome();
            case "2" -> addExpense();
            case "3" -> setBudget();
            case "4" -> walletService.printSummary(current);
            case "5" -> walletService.printBudgets(current);
            case "6" -> countByCategories();
            case "7" -> exportCSV();
            case "8" -> logout();
            default -> System.out.println("Некорректный ввод, выберите пункт 1–8.");
        }
    }

    private void addIncome() {
        System.out.print("Категория дохода: ");
        String cat = sc.nextLine();
        double sum = Validator.readPositive(sc, "Сумма: ");
        System.out.print("Комментарий: ");
        String note = sc.nextLine();
        walletService.addIncome(current, cat, sum, note);
    }

    private void addExpense() {
        System.out.print("Категория расхода: ");
        String cat = sc.nextLine();
        double sum = Validator.readPositive(sc, "Сумма: ");
        System.out.print("Комментарий: ");
        String note = sc.nextLine();
        walletService.addExpense(current, cat, sum, note);
    }

    private void setBudget() {
        System.out.print("Категория: ");
        String cat = sc.nextLine();
        double limit = Validator.readPositive(sc, "Лимит бюджета: ");
        walletService.setBudget(current, cat, limit);
        System.out.println("Бюджет установлен.");
    }

    private void countByCategories() {
        System.out.print("Введите категории через запятую: ");
        String[] parts = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        if (parts.length == 0) {
            System.out.println("Категории не указаны.");
            return;
        }

        walletService.countByCategories(current, parts);
    }

    private void exportCSV() {
        String path = reportService.exportToCSV(current);
        System.out.println("Отчёт сохранён в файл: " + path);
    }

    private void logout() {
        current.wallet.save(current.login);
        System.out.println("Данные сохранены. Выход из аккаунта " + current.login);
        current = null;
    }
}
