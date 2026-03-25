package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;

import java.util.Comparator;

import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.Person;

/**
 * Sorts persons in address book by their rating.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts persons by their rating.\n"
            + "Parameters: " + PREFIX_RATING + "ORDER (must be 'asc' or 'desc')\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_RATING + "desc";

    public static final String MESSAGE_SUCCESS = "Sorted all persons by rating %1$s";

    private final boolean isAscending;

    /**
     * Constructs a {@code SortCommand}.
     *
     * @param isAscending true if the sort order is ascending, false if descending.
     */
    public SortCommand(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Person> comparator = (p1, p2) ->
                p1.getRating().value.compareTo(p2.getRating().value);
        if (!isAscending) {
            comparator = comparator.reversed();
        }
        model.updateSortedPersonList(comparator);
        return new CommandResult(String.format(MESSAGE_SUCCESS, isAscending ? "ascending" : "descending"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return isAscending == otherSortCommand.isAscending;
    }
}
