package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DetailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Details(null));
    }

    @Test
    public void constructor_invalidDetail_throwsIllegalArgumentException() {
        String invalidDetail = "<script>";
        assertThrows(IllegalArgumentException.class, () -> new Details(invalidDetail));
    }

    @Test
    public void isValidDetail() {
        // null Details
        assertThrows(NullPointerException.class, () -> Details.isValidDetail(null));

        // invalid Details
        assertFalse(Details.isValidDetail("<>")); // banned symbols
        assertFalse(Details.isValidDetail("peter\\")); // contains banned symbols

        // valid Details
        assertTrue(Details.isValidDetail("Met at career fair")); // alphabets only
        assertTrue(Details.isValidDetail("Last contacted on 12/02/26")); // numbers only
        assertTrue(Details.isValidDetail("Part of summer 2026 intake, last contact on 26/01/26")); // long Details
    }

    @Test
    public void equals() {
        Details details = new Details("Valid Details");

        // same values -> returns true
        assertTrue(details.equals(new Details("Valid Details")));

        // same object -> returns true
        assertTrue(details.equals(details));

        // null -> returns false
        assertFalse(details.equals(null));

        // different types -> returns false
        assertFalse(details.equals(5.0f));

        // different values -> returns false
        assertFalse(details.equals(new Details("Other Valid Details")));
    }

    @Test
    public void validToString() {
        Details details = new Details("Valid Details");
        assertEquals(details.toString(), "Valid Details");
    }

}
