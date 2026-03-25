package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RatingConditionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RatingCondition(null));
    }

    @Test
    public void constructor_invalidCondition_throwsIllegalArgumentException() {
        String invalidCondition = "";
        assertThrows(IllegalArgumentException.class, () -> new RatingCondition(invalidCondition));

        // missing operator
        assertThrows(IllegalArgumentException.class, () -> new RatingCondition("5.0"));

        // invalid rating
        assertThrows(IllegalArgumentException.class, () -> new RatingCondition("< abc"));
    }

    @Test
    public void isValidCondition() {
        RatingCondition condition = new RatingCondition("< 5.0");
        assertTrue(condition.test(new Rating("4.0")));
        assertFalse(condition.test(new Rating("5.0")));

        condition = new RatingCondition("<= 5.0");
        assertTrue(condition.test(new Rating("5.0")));
        assertTrue(condition.test(new Rating("4.0")));
        assertFalse(condition.test(new Rating("6.0")));

        condition = new RatingCondition("> 5.0");
        assertTrue(condition.test(new Rating("6.0")));
        assertFalse(condition.test(new Rating("5.0")));

        condition = new RatingCondition(">= 5.0");
        assertTrue(condition.test(new Rating("5.0")));
        assertTrue(condition.test(new Rating("6.0")));
        assertFalse(condition.test(new Rating("4.0")));

        condition = new RatingCondition("== 5.0");
        assertTrue(condition.test(new Rating("5.0")));
        assertFalse(condition.test(new Rating("4.0")));
    }

    @Test
    public void equals() {
        RatingCondition condition1 = new RatingCondition("< 5.0");
        RatingCondition condition2 = new RatingCondition("< 5.0");
        RatingCondition condition3 = new RatingCondition("> 5.0");

        // same object -> returns true
        assertTrue(condition1.equals(condition1));

        // same values -> returns true
        assertTrue(condition1.equals(condition2));

        // different types -> returns false
        assertFalse(condition1.equals(1));

        // null -> returns false
        assertFalse(condition1.equals(null));

        // different condition -> returns false
        assertFalse(condition1.equals(condition3));
    }
}
