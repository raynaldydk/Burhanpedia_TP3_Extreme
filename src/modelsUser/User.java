package modelsUser;

import java.util.UUID;

public abstract class User {
    private UUID id;
    private String username;
    private String password;
    private String role;
    private long balance;

    public User(String username, String password, String role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = 0;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public long getBalance() {
        return balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
