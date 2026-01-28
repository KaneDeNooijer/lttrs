package me.kanedenooijer.lttrs.database.entities;

import me.kanedenooijer.lttrs.database.enums.Role;

/**
 * Represents an account in the system.
 *
 * @param id       The unique identifier of the account
 * @param username The username of the account
 * @param password The password of the account
 * @param name     The name of the account holder
 * @param role     The role of the account holder
 */
public record Account(
        int id,
        String username,
        String password,
        String name,
        Role role
) {
}
