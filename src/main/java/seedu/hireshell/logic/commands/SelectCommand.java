package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.BiConsumer;

import seedu.hireshell.commons.core.index.Index;
import seedu.hireshell.commons.util.ToStringBuilder;
import seedu.hireshell.logic.Messages;
import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.Person;

/**
 * Selects a person identified using it's displayed index from the address book, bringing up a detailed view in the ui.
 */
public class SelectCommand extends Command {
    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the person identified by the index number used in the displayed person list, bringing up "
            + "a bigger view with additional details\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Person: %1$s";

    private static BiConsumer<Person, Integer> onPersonSelected;

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Registers the UI callback that updates the detailed view for a selected person.
     */
    public static void setOnPersonSelected(BiConsumer<Person, Integer> callback) {
        onPersonSelected = callback;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToSelect = lastShownList.get(targetIndex.getZeroBased());

        if (onPersonSelected != null) {
            onPersonSelected.accept(personToSelect, targetIndex.getOneBased());
        }
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, Messages.format(personToSelect)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SelectCommand)) {
            return false;
        }

        SelectCommand otherSelectCommand = (SelectCommand) other;
        return targetIndex.equals(otherSelectCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
