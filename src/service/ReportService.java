package service;

import model.User;
import model.Transaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class ReportService {

    public String exportToCSV(User user) {
        String filename = user.login + "_report.csv";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            pw.println("Тип,Категория,Сумма,Комментарий,Дата");
            for (var t : user.wallet.transactions) {
                pw.printf("%s,%s,%.2f,%s,%s%n",
                        t.type,
                        t.category,
                        t.amount,
                        t.note.replace(",", " "),
                        fmt.format(t.createdAt));
            }

            pw.println();
            pw.printf("Общий доход,%.2f%n", user.wallet.totalIncome());
            pw.printf("Общие расходы,%.2f%n", user.wallet.totalExpense());
            pw.printf("Баланс,%.2f%n",
                    user.wallet.totalIncome() - user.wallet.totalExpense());

        } catch (IOException e) {
            System.out.println("Ошибка экспорта отчёта: " + e.getMessage());
        }

        return filename;
    }
}
