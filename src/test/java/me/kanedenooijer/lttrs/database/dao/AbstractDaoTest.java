package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.BaseTest;
import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractDaoTest extends BaseTest {

    AccountDao accountDao;

    @BeforeEach
    void setup() {
        accountDao = new AccountDao(connection);
    }

    @Test
    void givenExistingId_whenFindingById_thenReturnsEntity() throws SQLException {
        // Create test data
        insertTestUser("user1", "password1", "Test User 1", AccountRole.USER);

        // Find user
        Account testUser = accountDao.find(1).orElseThrow();

        // Verify its fields
        assertEquals(1, testUser.id());
        assertEquals("user1", testUser.username());
        assertEquals("password1", testUser.password());
        assertEquals("Test User 1", testUser.name());
        assertEquals(AccountRole.USER, testUser.role());
    }

    @Test
    void givenNonExistingId_whenFindingById_thenReturnsEmptyOptional() {
        // Dont find user
        Optional<Account> testUser = accountDao.find(1);

        // Verify empty optional
        assertEquals(Optional.empty(), testUser);
    }

    @Test
    void givenExistingEntities_whenFindingAll_thenReturnsList() throws SQLException {
        // Create test data
//        for (int i = 1; i <= 5; i++) {
//            insertTestUser(String.format("user%d", i), String.format("user%d@mail.com", i));
//        }

        // Find all users
        List<Account> users = accountDao.findAll();

        // Verify list size
        assertEquals(5, users.size());

        // Verify each users fields
        for (int i = 0; i < users.size(); i++) {
            Account testUser = users.get(i);
            int primaryKey = i + 1;

            assertEquals(primaryKey, testUser.id());
            assertEquals(String.format("user%d", primaryKey), testUser.username());
            assertEquals(String.format("password%d", primaryKey), testUser.password());
            assertEquals(String.format("Test User %d", primaryKey), testUser.name());
            assertEquals(AccountRole.USER, testUser.role());
        }
    }

    @Test
    void givenNoEntities_whenFindingAll_thenReturnsEmptyList() {
        // Find all users
        List<Account> users = accountDao.findAll();

        // Verify empty list
        assertEquals(0, users.size());
    }

    @Test
    void givenValidEntity_whenInserting_thenEntityIsPersisted() {
    }

    @Test
    void givenExistingEntity_whenUpdating_thenEntityIsUpdated() {
    }

    @Test
    void givenExistingId_whenDeleting_thenEntityIsRemoved() {
        // Create test data
//        insertTestUser("user1", "user1@mail.com");

        // Verify user is deleted
        assertTrue(accountDao.delete(1));
    }

}
