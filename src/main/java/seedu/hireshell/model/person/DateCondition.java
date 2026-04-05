package seedu.hireshell.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a condition for filtering dates.
 */
public class DateCondition {
    public static final String MESSAGE_CONSTRAINTS =
            "Date condition must start with an operator (before, after, on) followed by a valid date (YYYY-MM-DD).";

    private final String operator;
    private final LocalDate date;

    /**
     * Constructs a {@code DateCondition}.
     */
    public DateCondition(String condition) {
        requireNonNull(condition);
        condition = condition.trim().toLowerCase();
        String parsedOperator = "";
        String dateValue = "";

        if (condition.startsWith("before")) {
            parsedOperator = "before";
            dateValue = condition.substring(6).trim();
        } else if (condition.startsWith("after")) {
            parsedOperator = "after";
            dateValue = condition.substring(5).trim();
        } else if (condition.startsWith("on")) {
            parsedOperator = "on";
            dateValue = condition.substring(2).trim();
        } else {
            // Assume "on" operator if none specified
            parsedOperator = "on";
            dateValue = condition;
        }

        try {
            this.date = LocalDate.parse(dateValue);
            this.operator = parsedOperator;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Tests if the given date time matches the condition.
     */
    public boolean test(LocalDateTime dateTime) {
        LocalDate targetDate = dateTime.toLocalDate();
        switch (operator) {
        case "before":
            return targetDate.isBefore(date);
        case "after":
            return targetDate.isAfter(date);
        case "on":
            return targetDate.isEqual(date);
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateCondition)) {
            return false;
        }

        DateCondition otherCondition = (DateCondition) other;
        return operator.equals(otherCondition.operator) && date.equals(otherCondition.date);
    }
}
