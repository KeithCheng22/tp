package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.BatchDeleteCommand;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the BatchDeleteCommand code. For example, inputs " s/APPLIED" and " s/APPLIED abc" take the
 * same path through the BatchDeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class BatchDeleteCommandParserTest {

    private BatchDeleteCommandParser parser = new BatchDeleteCommandParser();

    @Test
    public void parse_validArgs_returnsBatchDeleteCommand() {
        // Status only
        BatchPredicate predicateStatus = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty());
        assertParseSuccess(parser, " s/APPLIED", new BatchDeleteCommand(predicateStatus));

        // Role only
        BatchPredicate predicateRole = new BatchPredicate(Optional.empty(),
                Optional.of(List.of(new Role("Developer"))), Optional.empty());
        assertParseSuccess(parser, " r/Developer", new BatchDeleteCommand(predicateRole));

        // Rating condition only
        BatchPredicate predicateRating = new BatchPredicate(Optional.empty(), Optional.empty(),
                Optional.of(new RatingCondition(">= 5.0")));
        assertParseSuccess(parser, " rt/>= 5.0", new BatchDeleteCommand(predicateRating));

        // All fields
        BatchPredicate predicateAll = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.of(List.of(new Role("Developer"))), Optional.of(new RatingCondition(">= 5.0")));
        assertParseSuccess(parser, " s/APPLIED r/Developer rt/>= 5.0", new BatchDeleteCommand(predicateAll));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // No arguments
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchDeleteCommand.MESSAGE_USAGE));

        // Invalid prefix alone does not match anything unless followed by preamble
        assertParseFailure(parser, " a/APPLIED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchDeleteCommand.MESSAGE_USAGE));

        // Invalid rating condition
        assertParseFailure(parser, " rt/invalid", RatingCondition.MESSAGE_CONSTRAINTS);
    }
}
