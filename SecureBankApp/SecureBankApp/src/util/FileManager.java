package util;

import model.Account;
import model.Transaction;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all reading and writing of data files (no database required).
 * Secure coding practice: all file I/O uses try-with-resources so streams
 * are always closed, even if an exception occurs.
 */
public final class FileManager {

    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.txt";
    private static final String ACCOUNTS_FILE = DATA_DIR + File.separator + "accounts.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + File.separator + "transactions.txt";

    private FileManager() {
    }

    /** Ensures the data directory and files exist before the app tries to use them. */
    public static void initialise() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            createIfMissing(USERS_FILE);
            createIfMissing(ACCOUNTS_FILE);
            createIfMissing(TRANSACTIONS_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialise data storage", e);
        }
    }

    private static void createIfMissing(String path) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
    }

    // ---------- Users ----------

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    users.add(User.fromFileLine(line));
                }
            }
        } catch (IOException e) {
            ConsoleColors.printError("Could not read users file: " + e.getMessage());
        }
        return users;
    }

    public static void appendUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.toFileLine());
            writer.newLine();
        } catch (IOException e) {
            ConsoleColors.printError("Could not save user: " + e.getMessage());
        }
    }

    // ---------- Accounts ----------

    public static List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    accounts.add(Account.fromFileLine(line));
                }
            }
        } catch (IOException e) {
            ConsoleColors.printError("Could not read accounts file: " + e.getMessage());
        }
        return accounts;
    }

    public static void appendAccount(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            writer.write(account.toFileLine());
            writer.newLine();
        } catch (IOException e) {
            ConsoleColors.printError("Could not save account: " + e.getMessage());
        }
    }

    /** Rewrites the entire accounts file - used after a balance changes. */
    public static void saveAllAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, false))) {
            for (Account acc : accounts) {
                writer.write(acc.toFileLine());
                writer.newLine();
            }
        } catch (IOException e) {
            ConsoleColors.printError("Could not update accounts file: " + e.getMessage());
        }
    }

    // ---------- Transactions ----------

    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    transactions.add(Transaction.fromFileLine(line));
                }
            }
        } catch (IOException e) {
            ConsoleColors.printError("Could not read transactions file: " + e.getMessage());
        }
        return transactions;
    }

    public static void appendTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(transaction.toFileLine());
            writer.newLine();
        } catch (IOException e) {
            ConsoleColors.printError("Could not save transaction: " + e.getMessage());
        }
    }
}
