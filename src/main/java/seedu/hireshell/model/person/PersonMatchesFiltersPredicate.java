package seedu.hireshell.model.person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.hireshell.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Rating}, {@code Status}, {@code createdAt}, and {@code roles} match the
 * filters given.
 */
public class PersonMatchesFiltersPredicate implements Predicate<Person> {
    private final RatingFilter ratingFilter;
    private final String statusFilter;
    private final DateFilter dateFilter;
    private final String roleFilter;

    /**
     * Represents a rating filter with an operator and a value.
     */
    public static class RatingFilter {
        /**
         * Operators for rating comparison.
         */
        public enum Operator {
            GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, EQUAL
        }

        private final Operator operator;
        private final double value;

        /**
         * Constructs a {@code RatingFilter}.
         *
         * @param operator The comparison operator.
         * @param value The rating value to compare against.
         */
        public RatingFilter(Operator operator, double value) {
            this.operator = operator;
            this.value = value;
        }

        /**
         * Tests if the given rating value matches the filter criteria.
         *
         * @param ratingValue The rating value to test.
         * @return true if the rating value matches the filter.
         */
        public boolean test(double ratingValue) {
            switch (operator) {
            case GREATER_THAN:
                return ratingValue > value;
            case GREATER_THAN_OR_EQUAL:
                return ratingValue >= value;
            case LESS_THAN:
                return ratingValue < value;
            case LESS_THAN_OR_EQUAL:
                return ratingValue <= value;
            case EQUAL:
                return Math.abs(ratingValue - value) < 0.0001;
            default:
                return false;
            }
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof RatingFilter)) {
                return false;
            }
            RatingFilter otherFilter = (RatingFilter) other;
            return operator == otherFilter.operator && value == otherFilter.value;
        }

        @Override
        public String toString() {
            return operator + " " + value;
        }
    }

    /**
     * Represents a date filter with an operator and a date value.
     */
    public static class DateFilter {
        /**
         * Operators for date comparison.
         */
        public enum Operator {
            BEFORE, AFTER, EQUAL
        }

        private final Operator operator;
        private final LocalDate date;

        /**
         * Constructs a {@code DateFilter}.
         *
         * @param operator The comparison operator (BEFORE, AFTER, or EQUAL).
         * @param date The date to compare against.
         */
        public DateFilter(Operator operator, LocalDate date) {
            this.operator = operator;
            this.date = date;
        }

        /**
         * Tests if the given date time matches the filter criteria (ignoring time).
         *
         * @param dateTime The date time to test.
         * @return true if the date part of the date time matches the filter.
         */
        public boolean test(LocalDateTime dateTime) {
            LocalDate targetDate = dateTime.toLocalDate();
            switch (operator) {
            case BEFORE:
                return targetDate.isBefore(date);
            case AFTER:
                return targetDate.isAfter(date);
            case EQUAL:
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
            if (!(other instanceof DateFilter)) {
                return false;
            }
            DateFilter otherFilter = (DateFilter) other;
            return operator == otherFilter.operator && Objects.equals(date, otherFilter.date);
        }

        @Override
        public String toString() {
            return operator + " " + date;
        }
    }

    /**
     * Constructs a {@code PersonMatchesFiltersPredicate}.
     *
     * @param ratingFilter The rating filter to apply, can be null.
     * @param statusFilter The status filter to apply, can be null.
     * @param dateFilter The date filter to apply, can be null.
     * @param roleFilter The role filter to apply, can be null.
     */
    public PersonMatchesFiltersPredicate(RatingFilter ratingFilter, String statusFilter, DateFilter dateFilter,
                                         String roleFilter) {
        this.ratingFilter = ratingFilter;
        this.statusFilter = statusFilter;
        this.dateFilter = dateFilter;
        this.roleFilter = roleFilter;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesRating = ratingFilter == null || ratingFilter.test(person.getRating().value);
        boolean matchesStatus = statusFilter == null || person.getStatus().value.toLowerCase()
                .contains(statusFilter.toLowerCase());
        boolean matchesDate = dateFilter == null || dateFilter.test(person.getCreatedAt());
        boolean matchesRole = roleFilter == null || person.getRoles().stream()
                .anyMatch(role -> role.roleName.toLowerCase().contains(roleFilter.toLowerCase()));
        return matchesRating && matchesStatus && matchesDate && matchesRole;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesFiltersPredicate)) {
            return false;
        }

        PersonMatchesFiltersPredicate otherPredicate = (PersonMatchesFiltersPredicate) other;
        return Objects.equals(ratingFilter, otherPredicate.ratingFilter)
                && Objects.equals(statusFilter, otherPredicate.statusFilter)
                && Objects.equals(dateFilter, otherPredicate.dateFilter)
                && Objects.equals(roleFilter, otherPredicate.roleFilter);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ratingFilter", ratingFilter)
                .add("statusFilter", statusFilter)
                .add("dateFilter", dateFilter)
                .add("roleFilter", roleFilter)
                .toString();
    }
}
