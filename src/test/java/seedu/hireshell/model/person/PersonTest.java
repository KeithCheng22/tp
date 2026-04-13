package seedu.hireshell.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_ROLE_HUSBAND;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_STATUS_BOB;
import static seedu.hireshell.testutil.Assert.assertThrows;
import static seedu.hireshell.testutil.TypicalPersons.ALICE;
import static seedu.hireshell.testutil.TypicalPersons.AMY;
import static seedu.hireshell.testutil.TypicalPersons.BOB;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.hireshell.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getRoles().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name and phone number, all other attributes different -> returns true
        Person editedAmy = new PersonBuilder(AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_STATUS_BOB).withRoles(VALID_ROLE_HUSBAND).build();
        assertTrue(AMY.isSamePerson(editedAmy));

        // different name, all other attributes same -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void metadata_behavior() {
        // Constructor with 8 arguments: createdAt should be around LocalDateTime.now()
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        Person person = new PersonBuilder().buildWithDefaultConstructor();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertTrue(person.getCreatedAt().isAfter(before) || person.getCreatedAt().isEqual(before));
        assertTrue(person.getCreatedAt().isBefore(after) || person.getCreatedAt().isEqual(after));

        // Constructor with 9 arguments: createdAt preserved
        LocalDateTime fixedCreatedAt = LocalDateTime.of(2020, 1, 1, 12, 0);
        Person editedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(), person.getRating(),
                person.getStatus(), person.getRoles(), person.getReferralStatus(), person.getDetails(), fixedCreatedAt);

        assertEquals(fixedCreatedAt, editedPerson.getCreatedAt());
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_STATUS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different roles -> returns false
        editedAlice = new PersonBuilder(ALICE).withRoles(VALID_ROLE_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different createdAt -> returns true
        editedAlice = new PersonBuilder(ALICE).withCreatedAt(ALICE.getCreatedAt().plusDays(1)).build();
        assertTrue(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", rating=" + ALICE.getRating()
                + ", status=" + ALICE.getStatus() + ", roles=" + ALICE.getRoles() + ", referralStatus="
                + ALICE.getReferralStatus() + ", details=" + ALICE.getDetails() + ", createdAt=" + ALICE.getCreatedAt()
                + "}";
        assertEquals(expected, ALICE.toString());
    }
}
