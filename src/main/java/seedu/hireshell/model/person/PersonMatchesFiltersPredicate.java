package seedu.hireshell.model.person;

import java.util.Objects;
import java.util.function.Predicate;

import seedu.hireshell.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Rating} and {@code Status} matches the filters given.
 */
public class PersonMatchesFiltersPredicate implements Predicate<Person> {
    private final RatingFilter ratingFilter;
    private final String statusFilter;

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
     * Constructs a {@code PersonMatchesFiltersPredicate}.
     *
     * @param ratingFilter The rating filter to apply, can be null.
     * @param statusFilter The status filter to apply, can be null.
     */
    public PersonMatchesFiltersPredicate(RatingFilter ratingFilter, String statusFilter) {
        this.ratingFilter = ratingFilter;
        this.statusFilter = statusFilter;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesRating = ratingFilter == null || ratingFilter.test(person.getRating().value);
        boolean matchesStatus = statusFilter == null || person.getStatus().value.equalsIgnoreCase(statusFilter);
        return matchesRating && matchesStatus;
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
                && Objects.equals(statusFilter, otherPredicate.statusFilter);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ratingFilter", ratingFilter)
                .add("statusFilter", statusFilter)
                .toString();
    }
}
