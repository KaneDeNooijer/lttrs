package me.kanedenooijer.lttrs.database.entity;

import java.time.LocalDate;

/**
 * A record representing a registration entry.
 *
 * @param id        The unique identifier of the registration
 * @param accountId The ID of the account associated with the registration
 * @param hours     The number of hours registered
 * @param date      The date of the registration
 */
public record Registration(
        int id,
        int accountId,
        int hours,
        LocalDate date
) {
}
