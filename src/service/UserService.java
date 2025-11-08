package service;

import model.User;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserService {

    private final Map<String, String> users = new HashMap<>();
    private final String USERS_FILE = "users.txt";

    public UserService() {
        loadUsers();
    }

    public boolean register(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            System.out.println("Логин и пароль не могут быть пустыми.");
            return false;
        }
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, password);
        saveUsers();
        return true;
    }

    public User login(String login, String password) {
        if (users.containsKey(login) && users.get(login).equals(password)) {
            return new User(login, password);
        }
        return null;
    }

    private void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 2) {
                    users.put(p[0], p[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки пользователей: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(USERS_FILE)) {
            for (var e : users.entrySet()) {
                pw.println(e.getKey() + "," + e.getValue());
            }
        } catch (Exception e) {
            System.out.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }
}
