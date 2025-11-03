package model;

public class User {
    public final String login;
    public final String password;
    public Wallet wallet;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = Wallet.load(login);
    }
}
