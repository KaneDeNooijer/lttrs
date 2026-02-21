package me.kanedenooijer.lttrs.database.entity;

import me.kanedenooijer.lttrs.type.AccountRole;

import java.util.Arrays;

/**
 * Represents an account in the system.
 *
 * @param id       The unique identifier of the account
 * @param fullName The full name of the account
 * @param password The password of the account
 * @param email    The name of the account holder
 * @param role     The role of the account holder
 */
public record Account(
        int id,
        String fullName,
        String email,
        String password,
        AccountRole role
) {
    public Account(String fullName, String email, String password, AccountRole role) {
        this(0, fullName, email, password, role);
    }

    /**
     * Returns the first name of the account holder by splitting the full name and taking the first part.
     *
     * @return The first name of the account holder
     */
    public String firstName() {
        return fullName.split(" ")[0];
    }

    /**
     * Returns the initials of the account holder by splitting the full name and taking the first character of each part.
     *
     * @return The initials of the account holder
     */
    public String initials() {
        String[] nameParts = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        Arrays.stream(nameParts).forEach(part -> initials.append(part.charAt(0)));

        return initials.toString().toUpperCase();
    }
}
