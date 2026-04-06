package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.BatchEditCommand;
import seedu.hireshell.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.DateCondition;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.testutil.EditPersonDescriptorBuilder;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the BatchEditCommand code. For example, inputs " s/APPLIED to s/REJECTED" and
 * " s/APPLIED to s/REJECTED abc" take the
 * same path through the BatchEditCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class BatchEditCommandParserTest {

    private BatchEditCommandParser parser = new BatchEditCommandParser();

    @Test
    public void parse_validArgs_returnsBatchEditCommand() {
        BatchPredicate predicate = new BatchPredicate(new Status("APPLIED"), null,
                new RatingCondition("> 4.0"), new DateCondition("before 2026-01-01"));
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress("REJECTED")
                .withDetails("Great candidate").withRoles().build();

        assertParseSuccess(parser, " s/APPLIED rt/> 4.0 dt/before 2026-01-01 to s/REJECTED d/Great candidate r/",
                new BatchEditCommand(predicate, descriptor));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // No 'to'
        assertParseFailure(parser, " s/APPLIED s/REJECTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));

        // No condition (missing left of 'to')
        assertParseFailure(parser, " to s/REJECTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));

        // No edits (missing right of 'to')
        assertParseFailure(parser, " s/APPLIED to ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));

        // Preamble present in condition
        assertParseFailure(parser, " some preamble s/APPLIED to s/REJECTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));

        // Invalid rating condition
        assertParseFailure(parser, " rt/invalid to s/REJECTED",
                seedu.hireshell.model.person.RatingCondition.MESSAGE_CONSTRAINTS);

        // Invalid date condition
        assertParseFailure(parser, " dt/invalid to s/REJECTED",
                seedu.hireshell.model.person.DateCondition.MESSAGE_CONSTRAINTS);

        // No valid condition fields
        assertParseFailure(parser, " n/Amy to s/REJECTED",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));

        // Invalid field to edit
        assertParseFailure(parser, " s/APPLIED to n/",
                seedu.hireshell.model.person.Name.MESSAGE_CONSTRAINTS);

        // No fields edited
        assertParseFailure(parser, " s/APPLIED to  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));
    }
}
