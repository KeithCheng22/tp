package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.role.Role;
import seedu.hireshell.testutil.PersonBuilder;

public class BatchPredicateTest {

    @Test
    public void test_emptyConditions_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_matchingStatus_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withAddress("APPLIED").build()));
    }

    @Test
    public void test_nonMatchingStatus_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withAddress("REJECTED").build()));
    }

    @Test
    public void test_matchingRole_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(),
                Optional.of(List.of(new Role("Developer"))), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withRoles("Developer", "Manager").build()));
    }

    @Test
    public void test_nonMatchingRole_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(),
                Optional.of(List.of(new Role("Developer"))), Optional.empty(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withRoles("Manager").build()));
    }

    @Test
    public void test_matchingRating_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.of(new RatingCondition("> 5.0")), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withRating("8.0").build()));
    }

    @Test
    public void test_nonMatchingRating_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.of(new RatingCondition("> 5.0")), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withRating("4.0").build()));
    }

    @Test
    public void test_matchingDate_returnsTrue() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(new DateCondition("before 2026-01-01")));
        assertTrue(predicate.test(new PersonBuilder()
                .withCreatedAt(java.time.LocalDateTime.of(2025, 12, 31, 23, 59)).build()));
    }

    @Test
    public void test_nonMatchingDate_returnsFalse() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(new DateCondition("before 2026-01-01")));
        assertFalse(predicate.test(new PersonBuilder()
                .withCreatedAt(java.time.LocalDateTime.of(2026, 1, 1, 0, 0)).build()));
    }

    @Test
    public void equals() {
        BatchPredicate predicate1 = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty(), Optional.empty());
        BatchPredicate predicate2 = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty(), Optional.empty());
        BatchPredicate predicate3 = new BatchPredicate(Optional.of(new Status("REJECTED")),
                Optional.empty(), Optional.empty(), Optional.empty());
        BatchPredicate predicate4 = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty(), Optional.of(new DateCondition("before 2026-01-01")));

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
