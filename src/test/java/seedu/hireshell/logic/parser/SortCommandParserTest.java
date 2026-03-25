package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
    public void parse_validArgs_returnsSortCommand() {
        // ascending
        assertParseSuccess(parser, " " + PREFIX_RATING + "asc", new SortCommand(true));

        // descending
        assertParseSuccess(parser, " " + PREFIX_RATING + "desc", new SortCommand(false));

        // case insensitive
        assertParseSuccess(parser, " " + PREFIX_RATING + "ASC", new SortCommand(true));
        assertParseSuccess(parser, " " + PREFIX_RATING + "Desc", new SortCommand(false));
    }

    @Test
    public void parse_invalidOrder_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING + "foo",
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
