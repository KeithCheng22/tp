package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.BatchDeleteCommand;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.DateCondition;
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
        BatchPredicate predicateStatus = new BatchPredicate(new Status("APPLIED"), null, null, null);
        assertParseSuccess(parser, " s/APPLIED", new BatchDeleteCommand(predicateStatus));

        // Role only
        BatchPredicate predicateRole = new BatchPredicate(null, List.of(new Role("Developer")), null, null);
        assertParseSuccess(parser, " r/Developer", new BatchDeleteCommand(predicateRole));

        // Rating condition only
        BatchPredicate predicateRating = new BatchPredicate(null, null, new RatingCondition(">= 5.0"), null);
        assertParseSuccess(parser, " rt/>= 5.0", new BatchDeleteCommand(predicateRating));

        // All fields
        BatchPredicate predicateAll = new BatchPredicate(new Status("APPLIED"),
                List.of(new Role("Developer")), new RatingCondition(">= 5.0"),
                new DateCondition("before 2026-01-01"));
        assertParseSuccess(parser, " s/APPLIED r/Developer rt/>= 5.0 dt/before 2026-01-01",
                new BatchDeleteCommand(predicateAll));

        // Date only
        BatchPredicate predicateDate = new BatchPredicate(null, null, null, new DateCondition("after 2023-10-01"));
        assertParseSuccess(parser, " dt/after 2023-10-01", new BatchDeleteCommand(predicateDate));
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

        // Invalid date condition
        assertParseFailure(parser, " dt/invalid", DateCondition.MESSAGE_CONSTRAINTS);
    }
}
