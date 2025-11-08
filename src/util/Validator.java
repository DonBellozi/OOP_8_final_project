package util;

import java.util.Scanner;

public class Validator {

    public static double readPositive(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Введите число больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число (например, 123.45).");
            }
        }
    }
}
