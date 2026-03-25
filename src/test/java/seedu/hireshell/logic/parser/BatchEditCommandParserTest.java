package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.BatchEditCommand;
import seedu.hireshell.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.testutil.EditPersonDescriptorBuilder;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the BatchEditCommand code. For example, inputs " s/APPLIED to s/REJECTED" and " s/APPLIED to s/REJECTED abc" take the
 * same path through the BatchEditCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class BatchEditCommandParserTest {

    private BatchEditCommandParser parser = new BatchEditCommandParser();

    @Test
    public void parse_validArgs_returnsBatchEditCommand() {
        BatchPredicate predicate = new BatchPredicate(Optional.of(new Status("APPLIED")),
                Optional.empty(), Optional.empty());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress("REJECTED").build();

        assertParseSuccess(parser, " s/APPLIED to s/REJECTED", new BatchEditCommand(predicate, descriptor));
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
    }
}
