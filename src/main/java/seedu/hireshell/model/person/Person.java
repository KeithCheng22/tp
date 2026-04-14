package seedu.hireshell.model.person;

import static seedu.hireshell.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.hireshell.commons.util.ToStringBuilder;
import seedu.hireshell.model.role.Role;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Rating rating;
    private final Status status;
    private final Set<Role> roles = new HashSet<>();
    private final ReferralStatus referralStatus;
    private final Details details;

    private final LocalDateTime createdAt;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Rating rating, Status status, Set<Role> roles,
                  ReferralStatus referralStatus, Details details) {
        requireAllNonNull(name, phone, email, status, roles, referralStatus);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.status = status;
        this.roles.addAll(roles);
        this.referralStatus = referralStatus;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor to be used for reading from storage and testing to copy full attributes.
     */
    public Person(Name name, Phone phone, Email email, Rating rating, Status status, Set<Role> roles,
                  ReferralStatus referralStatus, Details details, LocalDateTime createdAt) {
        requireAllNonNull(name, phone, email, status, roles, referralStatus);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.status = status;
        this.roles.addAll(roles);
        this.referralStatus = referralStatus;
        this.details = details;
        this.createdAt = createdAt;
    }



    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Rating getRating() {
        return rating;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Returns an immutable role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public ReferralStatus getReferralStatus() {
        return referralStatus;
    }

    public Details getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns true if both persons have the same name and phone number
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && (otherPerson.getPhone().equals(getPhone()) || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields
     * Date fields are excluded as they are metadata and shouldn't define business equality
     * This also ensures parser tests will not break due to the new fields
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && rating.equals(otherPerson.rating)
                && status.equals(otherPerson.status)
                && roles.equals(otherPerson.roles)
                && referralStatus.equals(otherPerson.referralStatus)
                && details.equals(otherPerson.details);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, rating, status, roles, referralStatus);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("rating", rating)
                .add("status", status)
                .add("roles", roles)
                .add("referralStatus", referralStatus)
                .add("details", details)
                .add("createdAt", createdAt)
                .toString();
    }
}
