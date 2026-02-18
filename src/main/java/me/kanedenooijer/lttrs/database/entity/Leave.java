package me.kanedenooijer.lttrs.database.entity;

import java.time.LocalDate;

/**
 * Represents a leave of absence for an employee.
 *
 * @param id        The unique identifier of the leave
 * @param accountId The ID of the account associated with the leave
 * @param startDate The start date of the leave
 * @param endDate   The end date of the leave
 */
public record Leave(
        int id,
        int accountId,
        LocalDate startDate,
        LocalDate endDate
) {
}
