package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.RatingFilter;

public class PersonMatchesFiltersPredicateTest {

    @Test
    public void test_matchesRating() {
        // GREATER_THAN_OR_EQUAL (already tested, but for completeness)
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN_OR_EQUAL, 7.0),
                null);

        Person personWithRating7 = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating7));

        // GREATER_THAN
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN, 7.0),
                null);
        assertFalse(predicate.test(personWithRating7));
        Person personWithRating8 = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating8));

        // LESS_THAN
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN, 5.0),
                null);
        Person personWithRating4 = new Person(new Name("Charlie"), new Phone("11111111"),
                new Email("charlie@example.com"), new Rating("4.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating4));
        assertFalse(predicate.test(personWithRating7));

        // LESS_THAN_OR_EQUAL
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN_OR_EQUAL, 5.0),
                null);
        Person personWithRating5 = new Person(new Name("David"), new Phone("22222222"),
                new Email("david@example.com"), new Rating("5.0"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating5));
        assertTrue(predicate.test(personWithRating4));

        // EQUAL
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.EQUAL, 8.5),
                null);
        assertTrue(predicate.test(personWithRating8));
        assertFalse(predicate.test(personWithRating7));
    }

    @Test
    public void test_matchesStatus() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                null,
                "Interviewing");

        Person personInterviewing = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Interviewing"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personInterviewing));

        Person personApplied = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertFalse(predicate.test(personApplied));
    }

    @Test
    public void test_matchesBoth() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN, 5.0),
                "Rejected");

        Person personRejectedPoorRating = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("4.5"), new Status("Rejected"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personRejectedPoorRating));

        Person personRejectedGoodRating = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Rejected"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertFalse(predicate.test(personRejectedGoodRating));
    }

    @Test
    public void test_equals() {
        RatingFilter ratingFilter1 = new RatingFilter(RatingFilter.Operator.EQUAL, 5.0);
        RatingFilter ratingFilter2 = new RatingFilter(RatingFilter.Operator.EQUAL, 5.0);
        RatingFilter ratingFilter3 = new RatingFilter(RatingFilter.Operator.GREATER_THAN, 5.0);

        // RatingFilter equals
        assertEquals(ratingFilter1, ratingFilter1);
        assertEquals(ratingFilter1, ratingFilter2);
        assertNotEquals(ratingFilter1, ratingFilter3);
        assertNotEquals(ratingFilter1, null);
        assertNotEquals(ratingFilter1, "string");

        PersonMatchesFiltersPredicate predicate1 = new PersonMatchesFiltersPredicate(ratingFilter1, "status");
        PersonMatchesFiltersPredicate predicate2 = new PersonMatchesFiltersPredicate(ratingFilter1, "status");
        PersonMatchesFiltersPredicate predicate3 = new PersonMatchesFiltersPredicate(ratingFilter3, "status");
        PersonMatchesFiltersPredicate predicate4 = new PersonMatchesFiltersPredicate(ratingFilter1, "other");

        // PersonMatchesFiltersPredicate equals
        assertEquals(predicate1, predicate1);
        assertEquals(predicate1, predicate2);
        assertNotEquals(predicate1, predicate3);
        assertNotEquals(predicate1, predicate4);
        assertNotEquals(predicate1, null);
        assertNotEquals(predicate1, "string");
    }

    @Test
    public void test_toString() {
        RatingFilter ratingFilter = new RatingFilter(RatingFilter.Operator.EQUAL, 5.0);
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(ratingFilter, "status");

        String expected = PersonMatchesFiltersPredicate.class.getCanonicalName()
                + "{ratingFilter=" + ratingFilter + ", statusFilter=status}";
        assertEquals(expected, predicate.toString());
    }
}
