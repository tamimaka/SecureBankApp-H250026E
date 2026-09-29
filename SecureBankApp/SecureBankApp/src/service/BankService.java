package service;

import model.Account;
import model.Transaction;
import util.FileManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles account creation, deposits, withdrawals, balance checks and
 * transaction history.
 */
public class BankService {

    private List<Account> accounts;

    public BankService() {
        this.accounts = FileManager.loadAccounts();
    }

    /** Generates a unique 10-digit account number that isn't already in use. */
    public String generateAccountNumber() {
        String candidate;
        do {
            long number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
            candidate = String.valueOf(number);
        } while (findByAccountNumber(candidate).isPresent());
        return candidate;
    }

    public Account createAccount(String accountNumber, String username, double openingBalance) {
        Account account = new Account(accountNumber, username, openingBalance);
        accounts.add(account);
        FileManager.appendAccount(account);
        return account;
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    /**
     * Deposits funds into an account. Amount must already be validated
     * as a positive number by the caller.
     */
    public boolean deposit(String accountNumber, double amount) {
        Optional<Account> accOpt = findByAccountNumber(accountNumber);
        if (accOpt.isEmpty()) {
            return false;
        }
        Account account = accOpt.get();
        account.setBalance(account.getBalance() + amount);
        FileManager.saveAllAccounts(accounts);
        FileManager.appendTransaction(
                Transaction.now(accountNumber, Transaction.Type.DEPOSIT, amount, account.getBalance()));
        return true;
    }

    /**
     * Withdraws funds from an account. Fails (returns false) if funds are
     * insufficient - overdrafts are not permitted.
     */
    public boolean withdraw(String accountNumber, double amount) {
        Optional<Account> accOpt = findByAccountNumber(accountNumber);
        if (accOpt.isEmpty()) {
            return false;
        }
        Account account = accOpt.get();
        if (account.getBalance() < amount) {
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        FileManager.saveAllAccounts(accounts);
        FileManager.appendTransaction(
                Transaction.now(accountNumber, Transaction.Type.WITHDRAWAL, amount, account.getBalance()));
        return true;
    }

    public List<Transaction> getHistory(String accountNumber) {
        return FileManager.loadTransactions().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .toList();
    }
}
