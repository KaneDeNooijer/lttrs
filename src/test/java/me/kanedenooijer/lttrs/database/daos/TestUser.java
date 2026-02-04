package me.kanedenooijer.lttrs.database.daos;

public record TestUser(
        int id,
        String username,
        String email
) {
    public TestUser(String username, String email) {
        this(0, username, email);
    }
}
