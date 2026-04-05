package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.DateFilter;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.RatingFilter;
import seedu.hireshell.model.role.Role;

public class PersonMatchesFiltersPredicateTest {

    @Test
    public void test_matchesRating() {
        // GREATER_THAN_OR_EQUAL
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN_OR_EQUAL, 7.0),
                null, null, null);

        Person personWithRating7 = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating7));

        // GREATER_THAN
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN, 7.0),
                null, null, null);
        assertFalse(predicate.test(personWithRating7));
        Person personWithRating8 = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating8));

        // LESS_THAN
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN, 5.0),
                null, null, null);
        Person personWithRating4 = new Person(new Name("Charlie"), new Phone("11111111"),
                new Email("charlie@example.com"), new Rating("4.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating4));
        assertFalse(predicate.test(personWithRating7));

        // LESS_THAN_OR_EQUAL
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN_OR_EQUAL, 5.0),
                null, null, null);
        Person personWithRating5 = new Person(new Name("David"), new Phone("22222222"),
                new Email("david@example.com"), new Rating("5.0"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRating5));
        assertTrue(predicate.test(personWithRating4));

        // EQUAL
        predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.EQUAL, 8.5),
                null, null, null);
        assertTrue(predicate.test(personWithRating8));
        assertFalse(predicate.test(personWithRating7));
    }

    @Test
    public void test_matchesStatus() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                null,
                "Interviewing", null, null);

        Person personInterviewing = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Interviewing"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personInterviewing));

        // Partial match
        predicate = new PersonMatchesFiltersPredicate(null, "Inter", null, null);
        assertTrue(predicate.test(personInterviewing));

        // Case-insensitive
        predicate = new PersonMatchesFiltersPredicate(null, "interview", null, null);
        assertTrue(predicate.test(personInterviewing));

        Person personApplied = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertFalse(predicate.test(personApplied));
    }

    @Test
    public void test_matchesDate() {
        LocalDate filterDate = LocalDate.of(2026, 4, 1);
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                null, null,
                new DateFilter(DateFilter.Operator.BEFORE, filterDate), null);

        Person personBefore = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 3, 31, 23, 59));
        assertTrue(predicate.test(personBefore));

        Person personSameDay = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 4, 1, 0, 0));
        assertFalse(predicate.test(personSameDay));

        // AFTER
        predicate = new PersonMatchesFiltersPredicate(
                null, null,
                new DateFilter(DateFilter.Operator.AFTER, filterDate), null);
        assertTrue(predicate.test(new Person(new Name("Charlie"), new Phone("11111111"),
                new Email("charlie@example.com"), new Rating("4.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 4, 2, 0, 0))));

        // EQUAL
        predicate = new PersonMatchesFiltersPredicate(
                null, null,
                new DateFilter(DateFilter.Operator.EQUAL, filterDate), null);
        assertTrue(predicate.test(personSameDay));
        assertFalse(predicate.test(personBefore));
    }

    @Test
    public void test_matchesRole() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                null, null, null, "Software Engineer");

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Software Engineer"));
        Person personWithRole = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("7.0"), new Status("Applied"),
                roles, ReferralStatus.fromString("yes"), new Details("valid details"));
        assertTrue(predicate.test(personWithRole));

        // Partial match
        predicate = new PersonMatchesFiltersPredicate(null, null, null, "soft");
        assertTrue(predicate.test(personWithRole));

        // Case-insensitive
        predicate = new PersonMatchesFiltersPredicate(null, null, null, "SOFTWARE");
        assertTrue(predicate.test(personWithRole));

        Person personWithoutRole = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("8.5"), new Status("Applied"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"));
        assertFalse(predicate.test(personWithoutRole));

        Set<Role> otherRoles = new HashSet<>();
        otherRoles.add(new Role("Analyst"));
        Person personWithOtherRole = new Person(new Name("Charlie"), new Phone("11111111"),
                new Email("charlie@example.com"), new Rating("4.5"), new Status("Applied"),
                otherRoles, ReferralStatus.fromString("yes"), new Details("valid details"));
        assertFalse(predicate.test(personWithOtherRole));
    }

    @Test
    public void test_matchesAll() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN, 8.0),
                "Accepted",
                new DateFilter(DateFilter.Operator.AFTER, LocalDate.of(2026, 1, 1)),
                "Software Engineer");

        Set<Role> roles = Collections.singleton(new Role("Software Engineer"));
        Person personMatch = new Person(new Name("Alice"), new Phone("12345678"),
                new Email("alice@example.com"), new Rating("8.5"), new Status("Accepted"),
                roles, ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 2, 1, 0, 0));
        assertTrue(predicate.test(personMatch));

        // Rating mismatch
        Person personRatingMismatch = new Person(new Name("Bob"), new Phone("87654321"),
                new Email("bob@example.com"), new Rating("7.5"), new Status("Accepted"),
                roles, ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 2, 1, 0, 0));
        assertFalse(predicate.test(personRatingMismatch));

        // Status mismatch
        Person personStatusMismatch = new Person(new Name("Charlie"), new Phone("11111111"),
                new Email("charlie@example.com"), new Rating("8.5"), new Status("Applied"),
                roles, ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 2, 1, 0, 0));
        assertFalse(predicate.test(personStatusMismatch));

        // Date mismatch
        Person personDateMismatch = new Person(new Name("David"), new Phone("22222222"),
                new Email("david@example.com"), new Rating("8.5"), new Status("Accepted"),
                roles, ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2025, 12, 31, 23, 59));
        assertFalse(predicate.test(personDateMismatch));

        // Role mismatch
        Person personRoleMismatch = new Person(new Name("Eve"), new Phone("33333333"),
                new Email("eve@example.com"), new Rating("8.5"), new Status("Accepted"),
                new HashSet<>(), ReferralStatus.fromString("yes"), new Details("valid details"),
                LocalDateTime.of(2026, 2, 1, 0, 0));
        assertFalse(predicate.test(personRoleMismatch));
    }

    @Test
    public void test_equals() {
        RatingFilter ratingFilter = new RatingFilter(RatingFilter.Operator.EQUAL, 5.0);
        DateFilter dateFilter = new DateFilter(DateFilter.Operator.BEFORE, LocalDate.of(2026, 1, 1));
        String roleFilter = "Software Engineer";

        // DateFilter equals
        DateFilter dateFilterSame = new DateFilter(DateFilter.Operator.BEFORE, LocalDate.of(2026, 1, 1));
        DateFilter dateFilterDiff = new DateFilter(DateFilter.Operator.AFTER, LocalDate.of(2026, 1, 1));
        DateFilter dateFilterEqual = new DateFilter(DateFilter.Operator.EQUAL, LocalDate.of(2026, 1, 1));
        assertEquals(dateFilter, dateFilter);
        assertEquals(dateFilter, dateFilterSame);
        assertNotEquals(dateFilter, dateFilterDiff);
        assertNotEquals(dateFilter, dateFilterEqual);
        assertNotEquals(dateFilter, null);
        assertNotEquals(dateFilter, "string");

        PersonMatchesFiltersPredicate predicate1 = new PersonMatchesFiltersPredicate(
                ratingFilter, "status", dateFilter, roleFilter);
        PersonMatchesFiltersPredicate predicate2 = new PersonMatchesFiltersPredicate(
                ratingFilter, "status", dateFilter, roleFilter);
        PersonMatchesFiltersPredicate predicate3 = new PersonMatchesFiltersPredicate(null, "status", dateFilter,
                roleFilter);
        PersonMatchesFiltersPredicate predicate4 = new PersonMatchesFiltersPredicate(ratingFilter, null, dateFilter,
                roleFilter);
        PersonMatchesFiltersPredicate predicate5 = new PersonMatchesFiltersPredicate(ratingFilter, "status", null,
                roleFilter);
        PersonMatchesFiltersPredicate predicate6 = new PersonMatchesFiltersPredicate(ratingFilter, "status",
                dateFilter, null);

        // PersonMatchesFiltersPredicate equals
        assertEquals(predicate1, predicate1);
        assertEquals(predicate1, predicate2);
        assertNotEquals(predicate1, predicate3);
        assertNotEquals(predicate1, predicate4);
        assertNotEquals(predicate1, predicate5);
        assertNotEquals(predicate1, predicate6);
        assertNotEquals(predicate1, null);
        assertNotEquals(predicate1, "string");
    }

    @Test
    public void test_toString() {
        RatingFilter ratingFilter = new RatingFilter(RatingFilter.Operator.EQUAL, 5.0);
        DateFilter dateFilter = new DateFilter(DateFilter.Operator.BEFORE, LocalDate.of(2026, 1, 1));
        String roleFilter = "Software Engineer";
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(ratingFilter, "status",
                dateFilter, roleFilter);

        String expected = PersonMatchesFiltersPredicate.class.getCanonicalName()
                + "{ratingFilter=" + ratingFilter + ", statusFilter=status, dateFilter=" + dateFilter
                + ", roleFilter=" + roleFilter + "}";
        assertEquals(expected, predicate.toString());
    }
}
