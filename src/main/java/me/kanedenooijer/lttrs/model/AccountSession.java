package me.kanedenooijer.lttrs.model;

import me.kanedenooijer.lttrs.database.entity.Account;

public final class AccountSession {

    private static AccountSession instance;

    private Account account;

    private AccountSession() {
    }

    public void login(Account account) {
        this.account = account;
    }

    public void logout() {
        this.account = null;
    }

    public boolean isLoggedIn() {
        return this.account != null;
    }

    public Account getAccount() {
        return this.account;
    }

    public static AccountSession getInstance() {
        if (AccountSession.instance == null) {
            AccountSession.instance = new AccountSession();
        }

        return AccountSession.instance;
    }
}
