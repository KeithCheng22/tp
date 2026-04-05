package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import seedu.hireshell.commons.util.ToStringBuilder;
import seedu.hireshell.logic.Messages;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate;

/**
 * Filters all persons in address book by their rating, status, date added and/or role.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters persons by their rating, status, date added and/or role.\n"
            + "Parameters: [" + PREFIX_RATING + "RATING] [" + PREFIX_STATUS + "STATUS] ["
            + PREFIX_DATE + "DATE_FILTER] [" + PREFIX_ROLE + "ROLE]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_RATING + ">= 7 " + PREFIX_STATUS
            + "Interviewing " + PREFIX_DATE + "after 2023-10-01 " + PREFIX_ROLE + "Software Engineer";

    private final PersonMatchesFiltersPredicate predicate;

    /**
     * Constructs a {@code FilterCommand}.
     *
     * @param predicate The predicate used to filter persons.
     */
    public FilterCommand(PersonMatchesFiltersPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
