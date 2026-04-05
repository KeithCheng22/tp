package seedu.hireshell.model.person;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.hireshell.model.role.Role;

/**
 * Tests that a {@code Person}'s attributes match all given criteria.
 */
public class BatchPredicate implements Predicate<Person> {
    private final Optional<Status> status;
    private final Optional<List<Role>> roles;
    private final Optional<RatingCondition> ratingCondition;
    private final Optional<DateCondition> dateCondition;

    /**
     * Constructs a {@code BatchPredicate} with the given conditions.
     */
    public BatchPredicate(Optional<Status> status, Optional<List<Role>> roles,
                          Optional<RatingCondition> ratingCondition,
                          Optional<DateCondition> dateCondition) {
        this.status = status;
        this.roles = roles;
        this.ratingCondition = ratingCondition;
        this.dateCondition = dateCondition;
    }

    @Override
    public boolean test(Person person) {
        boolean matchStatus = status.map(s -> person.getStatus().equals(s)).orElse(true);
        boolean matchRoles = roles.map(r -> person.getRoles().containsAll(r)).orElse(true);
        boolean matchRating = ratingCondition.map(rc -> rc.test(person.getRating())).orElse(true);
        boolean matchDate = dateCondition.map(dc -> dc.test(person.getCreatedAt())).orElse(true);

        return matchStatus && matchRoles && matchRating && matchDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BatchPredicate)) {
            return false;
        }

        BatchPredicate otherPredicate = (BatchPredicate) other;
        return status.equals(otherPredicate.status)
                && roles.equals(otherPredicate.roles)
                && ratingCondition.equals(otherPredicate.ratingCondition)
                && dateCondition.equals(otherPredicate.dateCondition);
    }
}
