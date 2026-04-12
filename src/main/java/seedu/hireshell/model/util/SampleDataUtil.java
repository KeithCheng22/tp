package seedu.hireshell.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.hireshell.model.AddressBook;
import seedu.hireshell.model.ReadOnlyAddressBook;
import seedu.hireshell.model.person.Details;
import seedu.hireshell.model.person.Email;
import seedu.hireshell.model.person.Name;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.Phone;
import seedu.hireshell.model.person.Rating;
import seedu.hireshell.model.person.ReferralStatus;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Rating("8.5"), new Status("Accepted"),
                getRoleSet("Software Engineer"), ReferralStatus.REFERRED,
                    new Details("")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Rating("9.0"), new Status("Offered"),
                getRoleSet("QA Engineer", "Software Engineer"), ReferralStatus.REFERRED,
                    new Details("Met at career fair")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Rating("6.0"), new Status("Offered"),
                getRoleSet("Database Engineer"), ReferralStatus.REFERRED,
                    new Details("Found through Linkedin")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Rating("7.0"), new Status("Accepted"),
                getRoleSet("UI Designer"), ReferralStatus.REFERRED,
                    new Details("Last contacted on 12/02/2026")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Rating("9.5"), new Status("Offered"),
                getRoleSet("Marketing Manager"), ReferralStatus.REFERRED,
                    new Details("")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Rating("5.5"), new Status("Rejected"),
                getRoleSet("QA Engineer"), ReferralStatus.REFERRED, new Details(""))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a role set containing the list of strings given.
     */
    public static Set<Role> getRoleSet(String... strings) {
        return Arrays.stream(strings)
                .map(Role::new)
                .collect(Collectors.toSet());
    }

}
