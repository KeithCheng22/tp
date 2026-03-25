package seedu.hireshell.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.ReadOnlyAddressBook;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.role.Role;

public class SampleDataUtilTest {
    @Test
    public void getSamplePersons_returnsExpectedSampleData() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(6, samplePersons.length);
        assertEquals("Alex Yeoh", samplePersons[0].getName().fullName);
        assertEquals("92624417", samplePersons[5].getPhone().value);
        assertEquals("Rejected", samplePersons[5].getStatus().toString());
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(samplePersons.length, sampleAddressBook.getPersonList().size());
        for (Person samplePerson : samplePersons) {
            assertTrue(sampleAddressBook.getPersonList().contains(samplePerson));
        }
    }

    @Test
    public void getRoleSet_duplicateRoles_removed() {
        Set<Role> roles = SampleDataUtil.getRoleSet("friends", "friends", "colleagues");

        assertEquals(2, roles.size());
        assertTrue(roles.contains(new Role("friends")));
        assertTrue(roles.contains(new Role("colleagues")));
        assertFalse(roles.contains(new Role("family")));
    }

}
