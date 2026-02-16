package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.BaseTest;
import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.Role;
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
        insertTestUser("user1", "password1", "Test User 1");

        // Find user
        Account testUser = accountDao.find(1).orElseThrow();

        // Verify its fields
        assertEquals(1, testUser.id());
        assertEquals("user1", testUser.username());
        assertEquals("password1", testUser.password());
        assertEquals("Test User 1", testUser.name());
        assertEquals(Role.USER, testUser.role());
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
        for (int i = 1; i <= 5; i++) {
            insertTestUser(
                    String.format("user%d", i),
                    String.format("password%d", i),
                    String.format("Test User %d", i)
            );
        }

        // Find all users
        List<Account> users = accountDao.findAll();

        // Verify list size
        assertEquals(5, users.size());

        // Verify each users fields
        for (int i = 0; i < users.size(); i++) {
            Account testUser = users.get(i);

            assertEquals(i + 1, testUser.id());
            assertEquals(String.format("user%d", i + 1), testUser.username());
            assertEquals(String.format("password%d", i + 1), testUser.password());
            assertEquals(String.format("Test User %d", i + 1), testUser.name());
            assertEquals(Role.USER, testUser.role());
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
    void givenExistingId_whenDeleting_thenEntityIsRemoved() throws SQLException {
        // Create test data
        insertTestUser("user1", "password1", "Test User 1");

        // Verify user is deleted
        assertTrue(accountDao.delete(1));
    }

}
