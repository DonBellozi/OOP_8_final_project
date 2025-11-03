package service;

import model.User;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class ReportService {

    public String exportToCSV(User user) {
        String filename = user.login + "_report.csv";
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "UTF-8"))) {
            pw.println("Тип,Категория,Сумма,Комментарий,Дата");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (var t : user.wallet.transactions) {
                pw.printf("%s,%s,%.2f,%s,%s%n",
                        t.type, t.category, t.amount, t.note.replace(",", " "),
                        fmt.format(t.createdAt));
            }
            pw.println();
            pw.printf("Доход,%.2f%n", user.wallet.totalIncome());
            pw.printf("Расход,%.2f%n", user.wallet.totalExpense());
            pw.printf("Баланс,%.2f%n", user.wallet.totalIncome() - user.wallet.totalExpense());
        } catch (IOException e) {
            return "Ошибка экспорта: " + e.getMessage();
        }
        return filename;
    }
}
