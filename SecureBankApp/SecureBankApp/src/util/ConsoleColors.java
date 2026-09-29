package util;

/**
 * Centralised ANSI colour palette for the application.
 * Theme: BLUE for normal/system output, MAROON for warnings & errors.
 *
 * Works in most terminals (VS Code integrated terminal, Windows Terminal,
 * macOS Terminal, Linux terminals). On very old Windows cmd.exe windows
 * ANSI codes may need to be enabled; VS Code's integrated terminal supports
 * them out of the box.
 */
public final class ConsoleColors {

    private ConsoleColors() {
        // utility class - prevent instantiation
    }

    public static final String RESET = "\u001B[0m";
    public static final String BOLD  = "\u001B[1m";

    // Primary theme colour - used for menus, prompts, headers, success messages
    public static final String BLUE = "\u001B[34m";
    public static final String BLUE_BOLD = "\u001B[1;34m";

    // Secondary theme colour (approximated maroon via 256-colour code)
    // used for warnings, errors and important alerts
    public static final String MAROON = "\u001B[38;5;88m";
    public static final String MAROON_BOLD = "\u001B[1;38;5;88m";

    public static void printHeader(String text) {
        System.out.println(BLUE_BOLD + "==================================================" + RESET);
        System.out.println(BLUE_BOLD + text + RESET);
        System.out.println(BLUE_BOLD + "==================================================" + RESET);
    }

    public static void printInfo(String text) {
        System.out.println(BLUE + text + RESET);
    }

    public static void printSuccess(String text) {
        System.out.println(BLUE_BOLD + text + RESET);
    }

    public static void printWarning(String text) {
        System.out.println(MAROON + text + RESET);
    }

    public static void printError(String text) {
        System.out.println(MAROON_BOLD + "ERROR: " + text + RESET);
    }

    public static void printPrompt(String text) {
        System.out.print(BLUE + text + RESET);
    }
}
