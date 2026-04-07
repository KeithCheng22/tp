package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.ArrayList;
import java.util.List;

import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.Person;

/**
 * Deletes multiple persons identified using matching predicates.
 */
public class BatchDeleteCommand extends Command {

    public static final String COMMAND_WORD = "batch delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all persons whose attributes match the specified condition(s).\n"
            + "Parameters: [" + PREFIX_STATUS + "STATUS] [" + PREFIX_ROLE + "ROLE]... "
            + "[" + PREFIX_RATING + "RATING_CONDITION] "
            + "[" + PREFIX_DATE + "DATE_CONDITION]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "Rejected " + PREFIX_RATING + "< 5 "
            + PREFIX_DATE + "before 2024-01-01";

    public static final String MESSAGE_BATCH_DELETE_SUCCESS = "Deleted %1$d person(s)";
    public static final String MESSAGE_NO_PERSONS_MATCHED = "No persons match the specified criteria";

    private final BatchPredicate predicate;

    public BatchDeleteCommand(BatchPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> personsToDelete = new ArrayList<>();
        for (Person person : new ArrayList<>(model.getFilteredPersonList())) {
            if (predicate.test(person)) {
                personsToDelete.add(person);
            }
        }

        if (personsToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSONS_MATCHED);
        }

        for (Person person : personsToDelete) {
            model.deletePerson(person);
        }

        return new CommandResult(String.format(MESSAGE_BATCH_DELETE_SUCCESS, personsToDelete.size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BatchDeleteCommand)) {
            return false;
        }

        BatchDeleteCommand otherBatchDeleteCommand = (BatchDeleteCommand) other;
        return predicate.equals(otherBatchDeleteCommand.predicate);
    }
}
