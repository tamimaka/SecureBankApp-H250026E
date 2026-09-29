package model;

/**
 * Represents a registered user of the banking application.
 * Stores only the salted hash of the password, never the plain text.
 */
public class User {

    private String username;
    private String salt;
    private String passwordHash;
    private String accountNumber;

    public User(String username, String salt, String passwordHash, String accountNumber) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.accountNumber = accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    /** Serialises this user to a single pipe-delimited line for file storage. */
    public String toFileLine() {
        return username + "|" + salt + "|" + passwordHash + "|" + accountNumber;
    }

    /** Parses a user back from a stored file line. */
    public static User fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Corrupt user record: " + line);
        }
        return new User(parts[0], parts[1], parts[2], parts[3]);
    }
}
