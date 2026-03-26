package seedu.hireshell.logic;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import seedu.hireshell.commons.core.GuiSettings;
import seedu.hireshell.logic.commands.CommandResult;
import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.logic.parser.exceptions.ParseException;
import seedu.hireshell.model.ReadOnlyAddressBook;
import seedu.hireshell.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.hireshell.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    void setSelectedPerson(Person person);
    Person getSelectedPerson();
    ReadOnlyObjectProperty<Person> selectedPersonProperty();
}
