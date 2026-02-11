package me.kanedenooijer.lttrs.util;

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

    /**
     * Converts a snake_case string to camelCase.
     *
     * @param input the string to convert
     * @return the converted string in camelCase
     */
    public static String toCamelCase(String input) {
        // TODO: write comments
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

}
