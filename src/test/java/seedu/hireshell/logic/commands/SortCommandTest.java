package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.hireshell.commons.core.GuiSettings;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.ReadOnlyAddressBook;
import seedu.hireshell.model.ReadOnlyUserPrefs;
import seedu.hireshell.model.person.Person;

public class SortCommandTest {

    @Test
    public void execute_sortSuccessful() {
        ModelStubAcceptingComparator modelStub = new ModelStubAcceptingComparator();
        SortCommand sortCommand = new SortCommand(true); // ascending

        CommandResult commandResult = sortCommand.execute(modelStub);

        assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, "ascending"),
                commandResult.getFeedbackToUser());
        requireNonNull(modelStub.comparatorUsed);
    }

    @Test
    public void equals() {
        SortCommand sortCommand1 = new SortCommand(true); // ascending
        SortCommand sortCommand2 = new SortCommand(true); // ascending
        SortCommand sortCommand3 = new SortCommand(false); // descending

        // same object -> returns true
        assertEquals(sortCommand1, sortCommand1);

        // same values -> returns true
        assertEquals(sortCommand1, sortCommand2);

        // different types -> returns false
        assertNotEquals(sortCommand1, 1);

        // null -> returns false
        assertNotEquals(sortCommand1, null);

        // different order -> returns false
        assertNotEquals(sortCommand1, sortCommand3);
    }

    /**
     * A Model stub that always accepts the comparator.
     */
    private class ModelStubAcceptingComparator extends ModelStub {
        private Comparator<Person> comparatorUsed;

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            this.comparatorUsed = comparator;
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
    }
}
