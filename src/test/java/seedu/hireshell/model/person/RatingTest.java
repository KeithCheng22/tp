package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RatingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Rating(null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        String invalidRating = "";
        assertThrows(IllegalArgumentException.class, () -> new Rating(invalidRating));
    }

    @Test
    public void isValidRating() {
        // null rating
        assertThrows(NullPointerException.class, () -> Rating.isValidRating(null));

        // invalid ratings
        assertFalse(Rating.isValidRating("")); // empty string
        assertFalse(Rating.isValidRating(" ")); // spaces only
        assertFalse(Rating.isValidRating("abc")); // non-numeric
        assertFalse(Rating.isValidRating("-1")); // negative number
        assertFalse(Rating.isValidRating("11")); // greater than 10
        assertFalse(Rating.isValidRating("10.1")); // greater than 10 with decimal
        assertFalse(Rating.isValidRating("5.5.5")); // multiple decimals

        // valid ratings
        assertTrue(Rating.isValidRating("0")); // zero
        assertTrue(Rating.isValidRating("5")); // single digit
        assertTrue(Rating.isValidRating("9")); // single digit upper boundary
        assertTrue(Rating.isValidRating("10")); // exactly 10
        assertTrue(Rating.isValidRating("0.0")); // zero with decimal
        assertTrue(Rating.isValidRating("8.5")); // standard decimal
        assertTrue(Rating.isValidRating("10.0")); // 10 with one trailing zero
    }

    @Test
    public void equals() {
        Rating rating = new Rating("8.5");

        // same values -> returns true
        assertTrue(rating.equals(new Rating("8.5")));

        // same object -> returns true
        assertTrue(rating.equals(rating));

        // logically same values but different string formats (parsed to Double) -> returns true
        assertTrue(new Rating("5").equals(new Rating("5.0")));

        // null -> returns false
        assertFalse(rating.equals(null));

        // different types -> returns false
        assertFalse(rating.equals(5.0f));

        // different values -> returns false
        assertFalse(rating.equals(new Rating("9.0")));
    }
}
