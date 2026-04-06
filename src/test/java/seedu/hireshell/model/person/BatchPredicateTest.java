package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.role.Role;
import seedu.hireshell.testutil.PersonBuilder;

public class BatchPredicateTest {

    @Test
    public void test_emptyConditions_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_matchingStatus_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(new Status("APPLIED"), null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withAddress("APPLIED").build()));
    }

    @Test
    public void test_nonMatchingStatus_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(new Status("APPLIED"), null, null, null);
        assertFalse(predicate.test(new PersonBuilder().withAddress("REJECTED").build()));
    }

    @Test
    public void test_matchingRole_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(null, List.of(new Role("Developer")), null, null);
        assertTrue(predicate.test(new PersonBuilder().withRoles("Developer", "Manager").build()));
    }

    @Test
    public void test_nonMatchingRole_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(null, List.of(new Role("Developer")), null, null);
        assertFalse(predicate.test(new PersonBuilder().withRoles("Manager").build()));
    }

    @Test
    public void test_matchingRating_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(null, null, new RatingCondition("> 5.0"), null);
        assertTrue(predicate.test(new PersonBuilder().withRating("8.0").build()));
    }

    @Test
    public void test_nonMatchingRating_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(null, null, new RatingCondition("> 5.0"), null);
        assertFalse(predicate.test(new PersonBuilder().withRating("4.0").build()));
    }

    @Test
    public void test_matchingDate_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(null, null, null, new DateCondition("before 2026-01-01"));
        assertTrue(predicate.test(new PersonBuilder()
                .withCreatedAt(java.time.LocalDateTime.of(2025, 12, 31, 23, 59)).build()));
    }

    @Test
    public void test_nonMatchingDate_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(null, null, null, new DateCondition("before 2026-01-01"));
        assertFalse(predicate.test(new PersonBuilder()
                .withCreatedAt(java.time.LocalDateTime.of(2026, 1, 1, 0, 0)).build()));
    }

    @Test
    public void equals() {
        BatchPredicate predicate1 = new BatchPredicate(new Status("APPLIED"), null, null, null);
        BatchPredicate predicate2 = new BatchPredicate(new Status("APPLIED"), null, null, null);
        BatchPredicate predicate3 = new BatchPredicate(new Status("REJECTED"), null, null, null);
        BatchPredicate predicate4 = new BatchPredicate(new Status("APPLIED"), null, null,
                new DateCondition("before 2026-01-01"));

        // same object -> returns true
        assertTrue(predicate1.equals(predicate1));

        // same values -> returns true
        assertTrue(predicate1.equals(predicate2));

        // different types -> returns false
        assertFalse(predicate1.equals(1));

        // null -> returns false
        assertFalse(predicate1.equals(null));

        // different status -> returns false
        assertFalse(predicate1.equals(predicate3));

        // different date -> returns false
        assertFalse(predicate1.equals(predicate4));
    }
}
