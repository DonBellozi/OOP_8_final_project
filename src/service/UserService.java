package service;

import model.User;

import java.io.*;
import java.util.*;

public class UserService {
    private final Map<String, String> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    public boolean register(String login, String password) {
        if (users.containsKey(login)) return false;
        users.put(login, password);
        saveUsers();
        return true;
    }

    public User login(String login, String password) {
        if (users.containsKey(login) && users.get(login).equals(password))
            return new User(login, password);
        return null;
    }

    private void loadUsers() {
        File f = new File("users.txt");
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",");
                if (p.length == 2) users.put(p[0], p[1]);
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки пользователей: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter("users.txt")) {
            for (var e : users.entrySet())
                pw.println(e.getKey() + "," + e.getValue());
        } catch (Exception e) {
            System.out.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }
}
