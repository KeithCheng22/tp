package seedu.hireshell.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.hireshell.model.person.Details;
import seedu.hireshell.model.person.Email;
import seedu.hireshell.model.person.Name;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.Phone;
import seedu.hireshell.model.person.Rating;
import seedu.hireshell.model.person.ReferralStatus;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;
import seedu.hireshell.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_RATING = "5.0";
    public static final String DEFAULT_STATUS = "Pending";
    public static final String DEFAULT_DETAIL = "Summer 2026 intake";


    private Name name;
    private Phone phone;
    private Email email;
    private Rating rating;
    private Set<Role> roles;
    private Status status;
    private ReferralStatus referralStatus;
    private Details details;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        rating = new Rating(DEFAULT_RATING);
        roles = new HashSet<>();
        status = new Status(DEFAULT_STATUS);
        referralStatus = ReferralStatus.NOT_REFERRED;
        details = new Details(DEFAULT_DETAIL);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        rating = personToCopy.getRating();
        roles = new HashSet<>(personToCopy.getRoles());
        status = personToCopy.getStatus();
        referralStatus = personToCopy.getReferralStatus();
        details = personToCopy.getDetails();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code roles} into a {@code Set<Role>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withRoles(String ... roles) {
        this.roles = SampleDataUtil.getRoleSet(roles);
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code Person} that we are building.
     */
    public PersonBuilder withRating(String rating) { // Added withRating method
        this.rating = new Rating(rating);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.status = new Status(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code ReferralStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withReferralStatus(ReferralStatus referralStatus) {
        this.referralStatus = referralStatus;
        return this;
    }

    /**
     * Optional convenience method to set referral status via string "yes"/"no".
     */
    public PersonBuilder withReferralStatus(String referralStatusString) {
        this.referralStatus = ReferralStatus.fromString(referralStatusString);
        return this;
    }

    /**
     * Sets the {@code Details} of the {@code Person} that we are building.
     * @param detail
     * @return
     */
    public PersonBuilder withDetail(String detail) {
        this.details = new Details(detail);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, rating, status, roles, referralStatus, details);
    }

}
