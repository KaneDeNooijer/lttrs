package me.kanedenooijer.lttrs.database.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic DAO Interface for CRUD operations.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public interface GenericDaoInterface<T, ID> {

    /**
     * Find an entity by its ID.
     *
     * @param id the identifier of the entity
     * @return an Optional containing the found entity or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Save an entity.
     *
     * @param entity the entity to be saved
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Update an entity.
     *
     * @param entity the entity to be updated
     * @return true if the update was successful, false otherwise
     */
    boolean update(T entity);

    /**
     * Delete an entity by its ID.
     *
     * @param id the identifier of the entity to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteById(ID id);

}
