package me.kanedenooijer.lttrs.database.entity;

import me.kanedenooijer.lttrs.type.AccountRole;

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
}
