package seedu.hireshell.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.hireshell.model.role.Role;

/**
 * Tests that a {@code Person}'s attributes match all given criteria.
 */
public class BatchPredicate implements Predicate<Person> {
    private final Status status;
    private final List<Role> roles;
    private final RatingCondition ratingCondition;
    private final DateCondition dateCondition;

    /**
     * Constructs a {@code BatchPredicate} with the given conditions.
     * Any parameter can be null to indicate no filter on that field.
     */
    public BatchPredicate(Status status, List<Role> roles,
                          RatingCondition ratingCondition, DateCondition dateCondition) {
        this.status = status;
        this.roles = roles;
        this.ratingCondition = ratingCondition;
        this.dateCondition = dateCondition;
    }

    @Override
    public boolean test(Person person) {
        boolean matchStatus = status == null || person.getStatus().value.equalsIgnoreCase(status.value);
        boolean matchRating = ratingCondition == null || ratingCondition.test(person.getRating());
        boolean matchDate = dateCondition == null || dateCondition.test(person.getCreatedAt());
        boolean matchRoles = true;
        if (roles != null) {
            List<String> personRoleNames = new ArrayList<>();
            for (Role role : person.getRoles()) {
                personRoleNames.add(role.roleName.toLowerCase());
            }
            List<String> filterRoleNames = new ArrayList<>();
            for (Role role : roles) {
                filterRoleNames.add(role.roleName.toLowerCase());
            }
            matchRoles = personRoleNames.containsAll(filterRoleNames);
        }

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
        return Objects.equals(status, otherPredicate.status)
                && Objects.equals(roles, otherPredicate.roles)
                && Objects.equals(ratingCondition, otherPredicate.ratingCondition)
                && Objects.equals(dateCondition, otherPredicate.dateCondition);
    }
}
