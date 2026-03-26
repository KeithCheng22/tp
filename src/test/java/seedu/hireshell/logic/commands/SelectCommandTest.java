package seedu.hireshell.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.hireshell.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.hireshell.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.hireshell.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.hireshell.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.hireshell.commons.core.index.Index;
import seedu.hireshell.logic.Messages;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.ModelManager;
import seedu.hireshell.model.UserPrefs;
import seedu.hireshell.model.person.Person;

public class SelectCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        // Avoid cross-test leakage from static callback.
        SelectCommand.setOnPersonSelected(null);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToSelect = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SelectCommand selectCommand = new SelectCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS,
                Messages.format(personToSelect));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(selectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToSelect = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SelectCommand selectCommand = new SelectCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS,
                Messages.format(personToSelect));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(selectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndex_invokesSelectionCallback() throws Exception {
        Person[] callbackPerson = new Person[1];
        Integer[] callbackIndex = new Integer[1];
        SelectCommand.setOnPersonSelected((person, index) -> {
            callbackPerson[0] = person;
            callbackIndex[0] = index;
        });

        SelectCommand selectCommand = new SelectCommand(INDEX_FIRST_PERSON);
        Person expectedSelectedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        CommandResult result = selectCommand.execute(model);

        assertEquals(String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS,
                Messages.format(expectedSelectedPerson)), result.getFeedbackToUser());
        assertEquals(expectedSelectedPerson, callbackPerson[0]);
        assertEquals(INDEX_FIRST_PERSON.getOneBased(), callbackIndex[0]);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SelectCommand selectCommand = new SelectCommand(outOfBoundIndex);

        assertCommandFailure(selectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        SelectCommand selectCommand = new SelectCommand(outOfBoundIndex);

        assertCommandFailure(selectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndex_doesNotInvokeSelectionCallback() {
        Person[] callbackPerson = new Person[1];
        Integer[] callbackIndex = new Integer[1];
        SelectCommand.setOnPersonSelected((person, index) -> {
            callbackPerson[0] = person;
            callbackIndex[0] = index;
        });

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SelectCommand selectCommand = new SelectCommand(outOfBoundIndex);

        assertCommandFailure(selectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertNull(callbackPerson[0]);
        assertNull(callbackIndex[0]);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(INDEX_FIRST_PERSON);
        SelectCommand selectSecondCommand = new SelectCommand(INDEX_SECOND_PERSON);

        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        SelectCommand selectFirstCommandCopy = new SelectCommand(INDEX_FIRST_PERSON);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        assertFalse(selectFirstCommand.equals(1));
        assertFalse(selectFirstCommand.equals((Object) null));
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        SelectCommand selectCommand = new SelectCommand(targetIndex);
        String expected = SelectCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, selectCommand.toString());
    }

}
