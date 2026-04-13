package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;

import java.util.Optional;

import seedu.hireshell.logic.commands.SortCommand;
import seedu.hireshell.logic.commands.SortCommand.SortType;
import seedu.hireshell.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @param args The arguments to parse.
     * @return The resulting SortCommand.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_RATING, PREFIX_DATE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_RATING, PREFIX_DATE);

        Optional<String> ratingOrder = argMultimap.getValue(PREFIX_RATING);
        Optional<String> dateOrder = argMultimap.getValue(PREFIX_DATE);

        if (ratingOrder.isPresent() && dateOrder.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        SortType sortType;
        String orderStr;

        if (ratingOrder.isPresent()) {
            sortType = SortType.RATING;
            orderStr = ratingOrder.get();
        } else if (dateOrder.isPresent()) {
            sortType = SortType.DATE;
            orderStr = dateOrder.get();
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        boolean isAscending;
        orderStr = orderStr.trim().toLowerCase();
        if (orderStr.equals("asc")) {
            isAscending = true;
        } else if (orderStr.equals("desc")) {
            isAscending = false;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(isAscending, sortType);
    }
}
