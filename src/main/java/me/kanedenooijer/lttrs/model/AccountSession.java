package me.kanedenooijer.lttrs.model;

import me.kanedenooijer.lttrs.database.entity.Account;

/**
 * Utility class holding the currently authenticated account for the duration of the session.
 */
public final class AccountSession {

    private static Account account;

    private AccountSession() {
    }

    /**
     * Stores the given account as the active session.
     *
     * @param account the account that has logged in
     */
    public static void login(Account account) {
        AccountSession.account = account;
    }

    /**
     * Clears the active session.
     */
    public static void logout() {
        account = null;
    }

    /**
     * Returns the currently authenticated account.
     */
    public static Account getAccount() {
        return account;
    }

}
