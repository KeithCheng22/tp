package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;

import java.util.Comparator;

import seedu.hireshell.commons.util.ToStringBuilder;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.Person;

/**
 * Sorts persons in address book by their rating or date added.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts persons by their rating or date added.\n"
            + "Parameters: " + PREFIX_RATING + "ORDER or " + PREFIX_DATE + "ORDER (ORDER must be 'asc' or 'desc')\n"
            + "Example 1: " + COMMAND_WORD + " " + PREFIX_RATING + "desc\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_DATE + "asc";

    public static final String MESSAGE_SUCCESS = "Sorted all persons by %1$s %2$s";

    /**
     * Enum for sorting types.
     */
    public enum SortType {
        RATING, DATE
    }

    private final boolean isAscending;
    private final SortType sortType;

    /**
     * Constructs a {@code SortCommand}.
     *
     * @param isAscending true if the sort order is ascending, false if descending.
     * @param sortType The field to sort by.
     */
    public SortCommand(boolean isAscending, SortType sortType) {
        this.isAscending = isAscending;
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Person> comparator;
        if (sortType == SortType.RATING) {
            comparator = (p1, p2) -> p1.getRating().value.compareTo(p2.getRating().value);
        } else {
            comparator = (p1, p2) -> p1.getCreatedAt().compareTo(p2.getCreatedAt());
        }

        if (!isAscending) {
            comparator = comparator.reversed();
        }
        model.updateSortedPersonList(comparator);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                sortType.name().toLowerCase(), isAscending ? "ascending" : "descending"));
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
        return isAscending == otherSortCommand.isAscending
                && sortType == otherSortCommand.sortType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("isAscending", isAscending)
                .add("sortType", sortType)
                .toString();
    }
}
