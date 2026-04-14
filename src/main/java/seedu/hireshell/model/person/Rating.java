package seedu.hireshell.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's rating in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRating(String)}
 */
public class Rating {

    public static final String MESSAGE_CONSTRAINTS =
            "Ratings should be a number between 0 and 10 (Up to 1 decimal place allowed).";
    public static final String VALIDATION_REGEX = "^([0-9](\\.\\d{1})?|10(\\.0{1})?)$";
    public final Double value;

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating string.
     */
    public Rating(String rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_CONSTRAINTS);
        value = Double.parseDouble(rating);
    }

    /**
     * Returns true if a given string is a valid rating.
     */
    public static boolean isValidRating(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Rating)) {
            return false;
        }

        Rating otherRating = (Rating) other;
        return value.equals(otherRating.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
