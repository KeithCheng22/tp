package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateConditionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateCondition(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DateCondition("before invalid-date"));
    }

    @Test
    public void test_before() {
        DateCondition condition = new DateCondition("before 2026-01-01");
        assertTrue(condition.test(LocalDateTime.of(2025, 12, 31, 23, 59)));
        assertFalse(condition.test(LocalDateTime.of(2026, 1, 1, 0, 0)));
    }

    @Test
    public void test_after() {
        DateCondition condition = new DateCondition("after 2026-01-01");
        assertTrue(condition.test(LocalDateTime.of(2026, 1, 2, 0, 0)));
        assertFalse(condition.test(LocalDateTime.of(2026, 1, 1, 0, 0)));
    }

    @Test
    public void test_on() {
        DateCondition condition = new DateCondition("on 2026-01-01");
        assertTrue(condition.test(LocalDateTime.of(2026, 1, 1, 12, 0)));
        assertFalse(condition.test(LocalDateTime.of(2026, 1, 2, 0, 0)));
    }

    @Test
    public void test_defaultOn() {
        DateCondition condition = new DateCondition("2026-01-01");
        assertTrue(condition.test(LocalDateTime.of(2026, 1, 1, 12, 0)));
    }

    @Test
    public void equals() {
        DateCondition condition = new DateCondition("before 2026-01-01");

        // same object -> returns true
        assertTrue(condition.equals(condition));

        // same values -> returns true
        assertTrue(condition.equals(new DateCondition("before 2026-01-01")));

        // different types -> returns false
        assertFalse(condition.equals(1));

        // null -> returns false
        assertFalse(condition.equals(null));

        // different operator -> returns false
        assertFalse(condition.equals(new DateCondition("after 2026-01-01")));

        // different date -> returns false
        assertFalse(condition.equals(new DateCondition("before 2025-01-01")));
    }
}
