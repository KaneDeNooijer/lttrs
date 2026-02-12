package me.kanedenooijer.lttrs.database.entity;

import me.kanedenooijer.lttrs.annotation.Column;
import me.kanedenooijer.lttrs.annotation.Id;
import me.kanedenooijer.lttrs.annotation.Table;
import me.kanedenooijer.lttrs.type.AccountRole;

/**
 * Represents an account in the system.
 *
 * @param id       The unique identifier of the account
 * @param username The username of the account
 * @param password The password of the account
 * @param name     The name of the account holder
 * @param role     The role of the account holder
 */
@Table("accounts")
public record Account(
        @Id
        @Column("id")
        int id,

        @Column("username")
        String username,

        @Column("password")
        String password,

        @Column("name")
        String name,

        @Column("role")
        AccountRole role
) {
}
