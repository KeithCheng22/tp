package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.hireshell.logic.commands.BatchDeleteCommand;
import seedu.hireshell.logic.parser.exceptions.ParseException;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.DateCondition;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;

/**
 * Parses input arguments and creates a new BatchDeleteCommand object
 */
public class BatchDeleteCommandParser implements Parser<BatchDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BatchDeleteCommand
     * and returns a BatchDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public BatchDeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STATUS, PREFIX_ROLE, PREFIX_RATING, PREFIX_DATE);

        if (!argMultimap.getPreamble().isEmpty() || args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchDeleteCommand.MESSAGE_USAGE));
        }

        Optional<Status> status = Optional.empty();
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            status = Optional.of(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_STATUS).get()));
        }

        Optional<List<Role>> roles = Optional.empty();
        if (!argMultimap.getAllValues(PREFIX_ROLE).isEmpty()) {
            Set<Role> roleSet = ParserUtil.parseRoles(argMultimap.getAllValues(PREFIX_ROLE));
            roles = Optional.of(List.copyOf(roleSet));
        }

        Optional<RatingCondition> ratingCondition = Optional.empty();
        if (argMultimap.getValue(PREFIX_RATING).isPresent()) {
            try {
                ratingCondition = Optional.of(new RatingCondition(argMultimap.getValue(PREFIX_RATING).get()));
            } catch (IllegalArgumentException e) {
                throw new ParseException(RatingCondition.MESSAGE_CONSTRAINTS);
            }
        }

        Optional<DateCondition> dateCondition = Optional.empty();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            try {
                dateCondition = Optional.of(new DateCondition(argMultimap.getValue(PREFIX_DATE).get()));
            } catch (IllegalArgumentException e) {
                throw new ParseException(DateCondition.MESSAGE_CONSTRAINTS);
            }
        }

        if (status.isEmpty() && roles.isEmpty() && ratingCondition.isEmpty() && dateCondition.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchDeleteCommand.MESSAGE_USAGE));
        }

        return new BatchDeleteCommand(new BatchPredicate(status, roles, ratingCondition, dateCondition));
    }
}
