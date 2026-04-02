package seedu.hireshell.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.hireshell.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.hireshell.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.hireshell.model.Model;
import seedu.hireshell.model.ModelManager;
import seedu.hireshell.model.UserPrefs;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code BatchDeleteCommand}.
 */
public class BatchDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPredicateMatching_success() {
        BatchPredicate predicate = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.of(new RatingCondition(">= 8.0")));
        BatchDeleteCommand command = new BatchDeleteCommand(predicate);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);
        int deletedCount = expectedModel.getFilteredPersonList().size();

        new ArrayList<>(expectedModel.getFilteredPersonList()).forEach(expectedModel::deletePerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        String expectedMessage = String.format(BatchDeleteCommand.MESSAGE_BATCH_DELETE_SUCCESS, deletedCount);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validPredicateNoMatching_throwsCommandException() {
        BatchPredicate predicate = new BatchPredicate(Optional.of(new Status("non-existent-status")),
                Optional.empty(), Optional.empty());
        BatchDeleteCommand command = new BatchDeleteCommand(predicate);

        assertCommandFailure(command, model, BatchDeleteCommand.MESSAGE_NO_PERSONS_MATCHED);
    }

    @Test
    public void equals() {
        BatchPredicate predicate1 = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty());
        BatchPredicate predicate2 = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty());
        BatchPredicate predicate3 = new BatchPredicate(Optional.of(new Status("REJECTED")),
                Optional.empty(), Optional.empty());

        BatchDeleteCommand command1 = new BatchDeleteCommand(predicate1);
        BatchDeleteCommand command2 = new BatchDeleteCommand(predicate2);
        BatchDeleteCommand command3 = new BatchDeleteCommand(predicate3);

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        assertTrue(command1.equals(command2));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different command -> returns false
        assertFalse(command1.equals(command3));
    }
}
