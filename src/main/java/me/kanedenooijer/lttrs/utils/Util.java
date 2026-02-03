package me.kanedenooijer.lttrs.utils;

/**
 * Utility class providing various helper methods.
 */
public final class Util {

    private Util() {
    }

    /**
     * Converts a string to snake_case.
     *
     * @param input the string to convert
     * @return the converted string in snake_case
     */
    public static String toSnakeCase(String input) {
        // Use StringBuilder for efficient string manipulation
        StringBuilder result = new StringBuilder();

        // Iterate through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            // Get the current character
            char c = input.charAt(i);

            // Add underscore if uppercase and it's not the first character
            if (Character.isUpperCase(c) && i > 0) {
                result.append('_');
            }

            // Append the character
            result.append(c);
        }

        return result.toString().toLowerCase();
    }

}
