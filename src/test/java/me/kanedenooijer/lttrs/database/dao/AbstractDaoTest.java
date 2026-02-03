package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractDaoTest extends BaseTest {

    TestUserDao testUserDao;

    @BeforeEach
    void setup() {
        testUserDao = new TestUserDao(connection, TestUser.class);
    }

    @Test
    void givenExistingId_whenFindingById_thenReturnsEntity() {
    }

    @Test
    void givenNonExistingId_whenFindingById_thenReturnsEmptyOptional() {
    }

    @Test
    void givenExistingEntities_whenFindingAll_thenReturnsList() {
    }

    @Test
    void givenNoEntities_whenFindingAll_thenReturnsEmptyList() {
    }

    @Test
    void givenValidEntity_whenInserting_thenEntityIsPersisted() {
    }

    @Test
    void givenExistingEntity_whenUpdating_thenEntityIsUpdated() {
    }

    @Test
    void givenExistingId_whenDeleting_thenEntityIsRemoved() {
    }

}
