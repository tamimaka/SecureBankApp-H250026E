package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Secure coding practice: passwords are never stored in plain text.
 * Each password is hashed with SHA-256 combined with a unique,
 * randomly generated salt per user (protects against rainbow-table attacks).
 */
public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    /** Generates a random 16-byte salt, Base64-encoded for safe storage in text files. */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /** Hashes a password with the given salt using SHA-256. */
    public static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }

    /** Verifies a plain-text password attempt against a stored salt + hash. */
    public static boolean verify(String passwordAttempt, String salt, String storedHash) {
        String attemptHash = hash(passwordAttempt, salt);
        return constantTimeEquals(attemptHash, storedHash);
    }

    /**
     * Constant-time comparison to reduce risk of timing attacks when
     * comparing hashes.
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
