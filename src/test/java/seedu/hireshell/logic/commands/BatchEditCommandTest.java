package seedu.hireshell.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.logic.commands.CommandTestUtil.VALID_STATUS_BOB;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.hireshell.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.ModelManager;
import seedu.hireshell.model.UserPrefs;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.testutil.EditPersonDescriptorBuilder;
import seedu.hireshell.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code BatchEditCommand}.
 */
public class BatchEditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPredicateMatching_success() {
        BatchPredicate predicate = new BatchPredicate(null, null, new RatingCondition(">= 8.0"), null);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_STATUS_BOB).build();
        BatchEditCommand command = new BatchEditCommand(predicate, descriptor);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        int editedCount = expectedModel.getFilteredPersonList().size();

        for (Person personToEdit : new ArrayList<>(expectedModel.getFilteredPersonList())) {
            Person editedPerson = new PersonBuilder(personToEdit).withAddress(VALID_STATUS_BOB).build();
            expectedModel.setPerson(personToEdit, editedPerson);
        }

        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        String expectedMessage = String.format(BatchEditCommand.MESSAGE_BATCH_EDIT_SUCCESS, editedCount);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validPredicateNoMatching_throwsCommandException() {
        BatchPredicate predicate = new BatchPredicate(new Status("non-existent-status"), null, null, null);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_STATUS_BOB).build();
        BatchEditCommand command = new BatchEditCommand(predicate, descriptor);

        assertCommandFailure(command, model, BatchEditCommand.MESSAGE_NO_PERSONS_MATCHED);
    }

    @Test
    public void equals() {
        BatchPredicate predicate1 = new BatchPredicate(new Status("APPLIED"), null, null, null);
        BatchPredicate predicate2 = new BatchPredicate(new Status("REJECTED"), null, null, null);

        EditPersonDescriptor descriptor1 = new EditPersonDescriptorBuilder().withAddress("ACCEPTED").build();
        EditPersonDescriptor descriptor2 = new EditPersonDescriptorBuilder().withAddress("REJECTED").build();

        BatchEditCommand command1 = new BatchEditCommand(predicate1, descriptor1);
        BatchEditCommand command2 = new BatchEditCommand(predicate1, descriptor1);
        BatchEditCommand command3 = new BatchEditCommand(predicate2, descriptor1);
        BatchEditCommand command4 = new BatchEditCommand(predicate1, descriptor2);

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        assertTrue(command1.equals(command2));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different predicate -> returns false
        assertFalse(command1.equals(command3));

        // different descriptor -> returns false
        assertFalse(command1.equals(command4));
    }
}
