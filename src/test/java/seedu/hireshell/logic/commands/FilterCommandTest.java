package seedu.hireshell.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import seedu.hireshell.commons.core.GuiSettings;
import seedu.hireshell.logic.Messages;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.ReadOnlyAddressBook;
import seedu.hireshell.model.ReadOnlyUserPrefs;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate;

public class FilterCommandTest {

    @Test
    public void execute_filterSuccessful() {
        ModelStubAcceptingPredicate modelStub = new ModelStubAcceptingPredicate();
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(null, "status", null, null);
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 0),
                commandResult.getFeedbackToUser());
        assertEquals(predicate, modelStub.predicateUsed);
    }

    @Test
    public void equals() {
        PersonMatchesFiltersPredicate predicate1 = new PersonMatchesFiltersPredicate(null, "status1", null, null);
        PersonMatchesFiltersPredicate predicate2 = new PersonMatchesFiltersPredicate(null, "status2", null, null);
        FilterCommand filterCommand1 = new FilterCommand(predicate1);
        FilterCommand filterCommand2 = new FilterCommand(predicate1);
        FilterCommand filterCommand3 = new FilterCommand(predicate2);

        // same object -> returns true
        assertEquals(filterCommand1, filterCommand1);

        // same values -> returns true
        assertEquals(filterCommand1, filterCommand2);

        // different types -> returns false
        assertNotEquals(1, filterCommand1);

        // null -> returns false
        assertNotEquals(null, filterCommand1);

        // different predicate -> returns false
        assertNotEquals(filterCommand1, filterCommand3);
    }

    @Test
    public void toStringMethod() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(null, "status", null, null);
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * A Model stub that always accepts the predicate.
     */
    private class ModelStubAcceptingPredicate extends ModelStub {
        private Predicate<Person> predicateUsed;

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            this.predicateUsed = predicate;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.emptyObservableList();
        }
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person getSelectedPerson() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyObjectProperty<Person> selectedPersonProperty() {
            throw new AssertionError("This method should not be called.");
        }
    }
}
