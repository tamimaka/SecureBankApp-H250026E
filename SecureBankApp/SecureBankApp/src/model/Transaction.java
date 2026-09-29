package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single deposit or withdrawal transaction.
 */
public class Transaction {

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String accountNumber;
    private Type type;
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Transaction(String accountNumber, Type type, double amount, double balanceAfter, String timestamp) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public static Transaction now(String accountNumber, Type type, double amount, double balanceAfter) {
        return new Transaction(accountNumber, type, amount, balanceAfter, LocalDateTime.now().format(FORMATTER));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String toFileLine() {
        return accountNumber + "|" + type + "|" + amount + "|" + balanceAfter + "|" + timestamp;
    }

    public static Transaction fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Corrupt transaction record: " + line);
        }
        return new Transaction(parts[0], Type.valueOf(parts[1]), Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]), parts[4]);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s %10.2f   (balance after: %.2f)", timestamp, type, amount, balanceAfter);
    }
}
