package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.BaseTest;
import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoTest extends BaseTest {

    AccountDao accountDao;

    @BeforeEach
    void setup() {
        this.accountDao = new AccountDao(this.connection);
    }

    @Test
    void givenValidAccount_whenCreating_thenReturnsCreatedAccount() {
        // Create account
        Account account = new Account("user1", "user1@mail.com", "password1", AccountRole.USER);
        Optional<Account> created = accountDao.create(account);

        // Verify it was returned with a generated ID
        assertTrue(created.isPresent());
        assertEquals(1, created.get().id());
        assertEquals("user1", created.get().fullName());
        assertEquals("user1@mail.com", created.get().email());
        assertEquals("password1", created.get().password());
        assertEquals(AccountRole.USER, created.get().role());
    }

    @Test
    void givenDuplicateEmail_whenCreating_thenThrowsRuntimeException() throws SQLException {
        // Insert existing user with same email
        insertTestUser("user1", "user1@mail.com", "password1");

        // Attempt to create another account with same email
        Account duplicate = new Account("user2", "user1@mail.com", "password2", AccountRole.USER);
        assertThrows(RuntimeException.class, () -> accountDao.create(duplicate));
    }

    @Test
    void givenExistingAccount_whenUpdating_thenReturnsTrueAndPersistsChanges() throws SQLException {
        // Insert existing user
        insertTestUser("user1", "user1@mail.com", "password1");

        // Update the account
        Account updated = new Account(1, "updated name", "updated@mail.com", "newpassword", AccountRole.ADMIN);
        assertTrue(accountDao.update(updated));

        // Verify the changes were persisted
        Account found = accountDao.find(1).orElseThrow();
        assertEquals("updated name", found.fullName());
        assertEquals("updated@mail.com", found.email());
        assertEquals("newpassword", found.password());
        assertEquals(AccountRole.ADMIN, found.role());
    }

    @Test
    void givenNonExistingAccount_whenUpdating_thenReturnsFalse() {
        // Attempt to update a non-existing account
        Account nonExisting = new Account(999, "ghost", "ghost@mail.com", "password", AccountRole.USER);
        assertFalse(accountDao.update(nonExisting));
    }

    @Test
    void givenExistingEmail_whenFindingByEmail_thenReturnsAccount() throws SQLException {
        // Insert existing user
        insertTestUser("user1", "user1@mail.com", "password1");

        // Find by email
        Account found = accountDao.findByEmail("user1@mail.com").orElseThrow();

        // Verify fields
        assertEquals("user1", found.fullName());
        assertEquals("user1@mail.com", found.email());
        assertEquals("password1", found.password());
        assertEquals(AccountRole.USER, found.role());
    }

    @Test
    void givenNonExistingEmail_whenFindingByEmail_thenReturnsEmptyOptional() {
        // Attempt to find a non-existing email
        Optional<Account> found = accountDao.findByEmail("ghost@mail.com");

        // Verify empty optional
        assertTrue(found.isEmpty());
    }

}
