package seedu.hireshell.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.commons.util.AppUtil.checkArgument;

/**
 * Represents a condition for filtering ratings.
 */
public class RatingCondition {
    public static final String MESSAGE_CONSTRAINTS =
            "Rating condition must start with an operator (<, <=, >, >=, ==) followed by a valid rating.";

    private final String operator;
    private final Rating rating;

    /**
     * Constructs a {@code RatingCondition}.
     */
    public RatingCondition(String condition) {
        requireNonNull(condition);
        condition = condition.trim();
        String parsedOperator = "";
        String ratingValue = "";

        if (condition.startsWith("<=")) {
            parsedOperator = "<=";
            ratingValue = condition.substring(2).trim();
        } else if (condition.startsWith(">=")) {
            parsedOperator = ">=";
            ratingValue = condition.substring(2).trim();
        } else if (condition.startsWith("==")) {
            parsedOperator = "==";
            ratingValue = condition.substring(2).trim();
        } else if (condition.startsWith("<")) {
            parsedOperator = "<";
            ratingValue = condition.substring(1).trim();
        } else if (condition.startsWith(">")) {
            parsedOperator = ">";
            ratingValue = condition.substring(1).trim();
        }

        checkArgument(!parsedOperator.isEmpty() && Rating.isValidRating(ratingValue), MESSAGE_CONSTRAINTS);
        this.operator = parsedOperator;
        this.rating = new Rating(ratingValue);
    }

    /**
     * Tests if the given rating matches the condition.
     */
    public boolean test(Rating otherRating) {
        double otherValue = otherRating.value;
        double targetValue = this.rating.value;

        switch (operator) {
        case "<":
            return otherValue < targetValue;
        case "<=":
            return otherValue <= targetValue;
        case ">":
            return otherValue > targetValue;
        case ">=":
            return otherValue >= targetValue;
        case "==":
            return otherValue == targetValue;
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
        if (!(other instanceof RatingCondition)) {
            return false;
        }

        RatingCondition otherCondition = (RatingCondition) other;
        return operator.equals(otherCondition.operator) && rating.equals(otherCondition.rating);
    }
}
