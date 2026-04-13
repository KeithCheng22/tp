package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.SortCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        assertParseFailure(parser, " abc " + PREFIX_RATING + "asc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        // rating sort
        assertParseSuccess(parser, " " + PREFIX_RATING + "asc", new SortCommand(true, SortCommand.SortType.RATING));
        assertParseSuccess(parser, " " + PREFIX_RATING + "desc", new SortCommand(false, SortCommand.SortType.RATING));

        // date sort
        assertParseSuccess(parser, " " + PREFIX_DATE + "asc", new SortCommand(true, SortCommand.SortType.DATE));
        assertParseSuccess(parser, " " + PREFIX_DATE + "desc", new SortCommand(false, SortCommand.SortType.DATE));

        // case insensitive
        assertParseSuccess(parser, " " + PREFIX_RATING + "ASC", new SortCommand(true, SortCommand.SortType.RATING));
        assertParseSuccess(parser, " " + PREFIX_DATE + "Desc", new SortCommand(false, SortCommand.SortType.DATE));
    }

    @Test
    public void parse_bothPrefixes_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING + "asc " + PREFIX_DATE + "desc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING + "asc " + PREFIX_RATING + "desc",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_RATING);
        assertParseFailure(parser, " " + PREFIX_DATE + "asc " + PREFIX_DATE + "desc",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_DATE);
    }

    @Test
    public void parse_invalidOrder_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING + "foo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " " + PREFIX_DATE + "bar",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noValue_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertParseFailure(parser, " n/asc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
