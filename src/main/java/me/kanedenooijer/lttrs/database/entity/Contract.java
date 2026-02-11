package me.kanedenooijer.lttrs.database.entity;

import java.time.LocalDate;

/**
 * Represents a labor contract in the system.
 *
 * @param id        The unique identifier of the contract
 * @param hours     The number of work hours per week
 * @param startDate The start date of the contract
 * @param endDate   The end date of the contract
 */
public record Contract(
        int id,
        int hours,
        LocalDate startDate,
        LocalDate endDate
) {
}
