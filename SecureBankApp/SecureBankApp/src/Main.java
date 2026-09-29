import model.Account;
import model.Transaction;
import model.User;
import service.AuthService;
import service.BankService;
import util.ConsoleColors;
import util.FileManager;
import util.InputValidator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Secure Banking Application - console entry point.
 *
 * 
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final BankService bankService = new BankService();

    private static User currentUser = null;

    public static void main(String[] args) {
        FileManager.initialise();

        ConsoleColors.printHeader("   SECURE BANKING APPLICATION   ");

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showAuthMenu();
            } else {
                running = showAccountMenu();
            }
        }

        ConsoleColors.printInfo("\nThank you for using Secure Banking Application. Goodbye!");
        scanner.close();
    }

    // ---------------- Authentication menu ----------------

    private static boolean showAuthMenu() {
        System.out.println();
        ConsoleColors.printInfo("1. Login");
        ConsoleColors.printInfo("2. Register new user");
        ConsoleColors.printInfo("3. Exit");
        ConsoleColors.printPrompt("Choose an option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                login();
                return true;
            case "2":
                register();
                return true;
            case "3":
                return false;
            default:
                ConsoleColors.printWarning("Invalid option, please try again.");
                return true;
        }
    }

    private static void login() {
        ConsoleColors.printPrompt("Username: ");
        String username = scanner.nextLine().trim();
        ConsoleColors.printPrompt("Password: ");
        String password = scanner.nextLine();

        Optional<User> result = authService.login(username, password);
        if (result.isPresent()) {
            currentUser = result.get();
            ConsoleColors.printSuccess("Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            ConsoleColors.printError("Invalid username or password.");
        }
    }

    private static void register() {
        ConsoleColors.printPrompt("Choose a username (letters/digits/underscore, min 3 chars): ");
        String username = scanner.nextLine().trim();

        if (!InputValidator.isValidUsername(username)) {
            ConsoleColors.printError("Invalid username format.");
            return;
        }
        if (authService.usernameExists(username)) {
            ConsoleColors.printError("That username is already taken.");
            return;
        }

        ConsoleColors.printPrompt("Choose a password (min 8 chars, letters + numbers): ");
        String password = scanner.nextLine();
        if (!InputValidator.isValidPassword(password)) {
            ConsoleColors.printError("Password does not meet security requirements.");
            return;
        }

        ConsoleColors.printPrompt("Opening deposit amount: ");
        String amountStr = scanner.nextLine().trim();
        if (!InputValidator.isValidAmount(amountStr)) {
            ConsoleColors.printError("Invalid opening deposit amount.");
            return;
        }
        double openingBalance = Double.parseDouble(amountStr);

        String accountNumber = bankService.generateAccountNumber();
        bankService.createAccount(accountNumber, username, openingBalance);
        authService.register(username, password, accountNumber);

        ConsoleColors.printSuccess("Registration successful!");
        ConsoleColors.printSuccess("Your new account number is: " + accountNumber);
        ConsoleColors.printInfo("You can now log in using your username and password.");
    }

    // ---------------- Account menu (after login) ----------------

    private static boolean showAccountMenu() {
        System.out.println();
        ConsoleColors.printHeader("Account Menu - " + currentUser.getUsername());
        ConsoleColors.printInfo("1. View balance");
        ConsoleColors.printInfo("2. Deposit funds");
        ConsoleColors.printInfo("3. Withdraw funds");
        ConsoleColors.printInfo("4. View transaction history");
        ConsoleColors.printInfo("5. Log out");
        ConsoleColors.printInfo("6. Exit");
        ConsoleColors.printPrompt("Choose an option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                viewBalance();
                return true;
            case "2":
                depositFunds();
                return true;
            case "3":
                withdrawFunds();
                return true;
            case "4":
                viewHistory();
                return true;
            case "5":
                currentUser = null;
                ConsoleColors.printInfo("Logged out.");
                return true;
            case "6":
                return false;
            default:
                ConsoleColors.printWarning("Invalid option, please try again.");
                return true;
        }
    }

    private static void viewBalance() {
        Optional<Account> account = bankService.findByAccountNumber(currentUser.getAccountNumber());
        account.ifPresentOrElse(
                a -> ConsoleColors.printSuccess(String.format("Current balance: %.2f", a.getBalance())),
                () -> ConsoleColors.printError("Account not found.")
        );
    }

    private static void depositFunds() {
        ConsoleColors.printPrompt("Amount to deposit: ");
        String amountStr = scanner.nextLine().trim();
        if (!InputValidator.isValidAmount(amountStr)) {
            ConsoleColors.printError("Invalid amount.");
            return;
        }
        double amount = Double.parseDouble(amountStr);
        boolean ok = bankService.deposit(currentUser.getAccountNumber(), amount);
        if (ok) {
            ConsoleColors.printSuccess(String.format("Deposited %.2f successfully.", amount));
        } else {
            ConsoleColors.printError("Deposit failed. Account not found.");
        }
    }

    private static void withdrawFunds() {
        ConsoleColors.printPrompt("Amount to withdraw: ");
        String amountStr = scanner.nextLine().trim();
        if (!InputValidator.isValidAmount(amountStr)) {
            ConsoleColors.printError("Invalid amount.");
            return;
        }
        double amount = Double.parseDouble(amountStr);
        boolean ok = bankService.withdraw(currentUser.getAccountNumber(), amount);
        if (ok) {
            ConsoleColors.printSuccess(String.format("Withdrew %.2f successfully.", amount));
        } else {
            ConsoleColors.printWarning("Withdrawal failed - insufficient funds or account not found.");
        }
    }

    private static void viewHistory() {
        List<Transaction> history = bankService.getHistory(currentUser.getAccountNumber());
        if (history.isEmpty()) {
            ConsoleColors.printInfo("No transactions yet.");
            return;
        }
        ConsoleColors.printHeader("Transaction History");
        for (Transaction t : history) {
            ConsoleColors.printInfo(t.toString());
        }
    }
}
