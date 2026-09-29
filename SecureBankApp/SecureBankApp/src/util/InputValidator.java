package util;

/**
 * Basic input validation helpers.
 * Secure coding practice: never trust raw user input - validate before use.
 */
public final class InputValidator {

    private InputValidator() {
    }

    public static boolean isValidUsername(String username) {
        return username != null
                && username.trim().length() >= 3
                && username.matches("^[a-zA-Z0-9_]+$");
    }

    public static boolean isValidPassword(String password) {
        // Minimum 8 chars, at least one letter and one digit
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Za-z].*")
                && password.matches(".*\\d.*");
    }

    public static boolean isValidAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0 && amount < 1_000_000_000.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
