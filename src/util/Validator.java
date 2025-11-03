package util;

import java.util.Scanner;

public class Validator {
    public static double readPositive(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine());
                if (val > 0) return val;
                System.out.println("Введите число больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число (например, 123.45).");
            }
        }
    }
}
