package service;

import model.User;
import util.FileManager;
import util.PasswordUtil;

import java.util.List;
import java.util.Optional;

/**
 * Handles user registration and login (authentication).
 */
public class AuthService {

    private List<User> users;

    public AuthService() {
        this.users = FileManager.loadUsers();
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Registers a new user with a securely hashed password and links them
     * to the given account number.
     */
    public User register(String username, String plainPassword, String accountNumber) {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash(plainPassword, salt);
        User user = new User(username, salt, hash, accountNumber);
        users.add(user);
        FileManager.appendUser(user);
        return user;
    }

    /**
     * Attempts to authenticate a user. Returns the User on success,
     * or empty if the credentials are invalid. Deliberately gives no
     * hint as to whether the username or the password was wrong,
     * to avoid username enumeration.
     */
    public Optional<User> login(String username, String plainPassword) {
        Optional<User> match = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (match.isEmpty()) {
            return Optional.empty();
        }

        User user = match.get();
        boolean ok = PasswordUtil.verify(plainPassword, user.getSalt(), user.getPasswordHash());
        return ok ? Optional.of(user) : Optional.empty();
    }
}
