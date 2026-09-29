package model;

/**
 * Represents a bank account belonging to a user.
 */
public class Account {

    private String accountNumber;
    private String ownerUsername;
    private double balance;

    public Account(String accountNumber, String ownerUsername, double balance) {
        this.accountNumber = accountNumber;
        this.ownerUsername = ownerUsername;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String toFileLine() {
        return accountNumber + "|" + ownerUsername + "|" + balance;
    }

    public static Account fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Corrupt account record: " + line);
        }
        return new Account(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}
