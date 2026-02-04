package me.kanedenooijer.lttrs.database.daos;

import me.kanedenooijer.lttrs.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractDaoTest extends BaseTest {

    TestUserDao testUserDao;

    @BeforeEach
    void setup() {
        testUserDao = new TestUserDao(connection, TestUser.class);
    }

    @Test
    void givenExistingId_whenFindingById_thenReturnsEntity() throws SQLException {
        // Create test data
        insertTestUser("user1", "user1@mail.com");

        // Find user
        TestUser testUser = testUserDao.find(1).orElseThrow();

        // Verify its fields
        assertEquals(1, testUser.id());
        assertEquals("user1", testUser.username());
        assertEquals("user1@mail.com", testUser.email());
    }

    @Test
    void givenNonExistingId_whenFindingById_thenReturnsEmptyOptional() {
        // Dont find user
        Optional<TestUser> testUser = testUserDao.find(1);

        // Verify empty optional
        assertEquals(Optional.empty(), testUser);
    }

    @Test
    void givenExistingEntities_whenFindingAll_thenReturnsList() throws SQLException {
        // Create test data
        for (int i = 1; i <= 5; i++) {
            insertTestUser(String.format("user%d", i), String.format("user%d@mail.com", i));
        }

        // Find all users
        List<TestUser> users = testUserDao.findAll();

        // Verify list size
        assertEquals(5, users.size());

        // Verify each users fields
        for (int i = 0; i < users.size(); i++) {
            TestUser testUser = users.get(i);
            int primaryKey = i + 1;

            assertEquals(primaryKey, testUser.id());
            assertEquals(String.format("user%d", primaryKey), testUser.username());
            assertEquals(String.format("user%d@mail.com", primaryKey), testUser.email());
        }
    }

    @Test
    void givenNoEntities_whenFindingAll_thenReturnsEmptyList() {
        // Find all users
        List<TestUser> users = testUserDao.findAll();

        // Verify empty list
        assertEquals(0, users.size());
    }

    @Test
    void givenValidEntity_whenInserting_thenEntityIsPersisted() {
        // Save user
        TestUser testUser = testUserDao.save(new TestUser(
                "user1",
                "user1@mail.com"
        ));

        // Verify its fields
        assertEquals(1, testUser.id());
        assertEquals("user1", testUser.username());
        assertEquals("user1@mail.com", testUser.email());
    }

    @Test
    void givenExistingEntity_whenUpdating_thenEntityIsUpdated() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Test
    void givenExistingId_whenDeleting_thenEntityIsRemoved() throws SQLException {
        // Create test data
        insertTestUser("user1", "user1@mail.com");

        // Verify user is deleted
        assertTrue(testUserDao.delete(1));
        assertEquals(Optional.empty(), testUserDao.find(1));
    }

}
