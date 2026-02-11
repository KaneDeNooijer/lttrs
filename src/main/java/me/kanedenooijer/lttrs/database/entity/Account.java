package me.kanedenooijer.lttrs.database.entity;

import me.kanedenooijer.lttrs.database.type.AccountRole;

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
        AccountRole role
) {
}
