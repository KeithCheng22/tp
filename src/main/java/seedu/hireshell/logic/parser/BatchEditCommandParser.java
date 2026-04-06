package seedu.hireshell.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_REFERRAL_STATUS;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.hireshell.logic.commands.BatchEditCommand;
import seedu.hireshell.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.hireshell.logic.parser.exceptions.ParseException;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.DateCondition;
import seedu.hireshell.model.person.RatingCondition;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;

/**
 * Parses input arguments and creates a new BatchEditCommand object
 */
public class BatchEditCommandParser implements Parser<BatchEditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BatchEditCommand
     * and returns a BatchEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public BatchEditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        String[] splitArgs = trimmedArgs.split(" to ");
        if (splitArgs.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));
        }

        String conditionArgs = " " + splitArgs[0].trim();
        String editArgs = " " + splitArgs[1].trim();

        // 1. Parse conditions
        ArgumentMultimap conditionMultimap =
                ArgumentTokenizer.tokenize(conditionArgs, PREFIX_STATUS, PREFIX_ROLE, PREFIX_RATING, PREFIX_DATE);

        if (!conditionMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));
        }

        Status status = null;
        if (conditionMultimap.getValue(PREFIX_STATUS).isPresent()) {
            status = ParserUtil.parseAddress(conditionMultimap.getValue(PREFIX_STATUS).get());
        }

        List<Role> roles = null;
        if (!conditionMultimap.getAllValues(PREFIX_ROLE).isEmpty()) {
            Set<Role> roleSet = ParserUtil.parseRoles(conditionMultimap.getAllValues(PREFIX_ROLE));
            roles = List.copyOf(roleSet);
        }

        RatingCondition ratingCondition = null;
        if (conditionMultimap.getValue(PREFIX_RATING).isPresent()) {
            try {
                ratingCondition = new RatingCondition(conditionMultimap.getValue(PREFIX_RATING).get());
            } catch (IllegalArgumentException e) {
                throw new ParseException(RatingCondition.MESSAGE_CONSTRAINTS);
            }
        }

        DateCondition dateCondition = null;
        if (conditionMultimap.getValue(PREFIX_DATE).isPresent()) {
            try {
                dateCondition = new DateCondition(conditionMultimap.getValue(PREFIX_DATE).get());
            } catch (IllegalArgumentException e) {
                throw new ParseException(DateCondition.MESSAGE_CONSTRAINTS);
            }
        }

        if (status == null && roles == null && ratingCondition == null && dateCondition == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BatchEditCommand.MESSAGE_USAGE));
        }

        BatchPredicate predicate = new BatchPredicate(status, roles, ratingCondition, dateCondition);

        // 2. Parse edit descriptor
        ArgumentMultimap editMultimap =
                ArgumentTokenizer.tokenize(editArgs, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_RATING, PREFIX_STATUS, PREFIX_ROLE, PREFIX_REFERRAL_STATUS, PREFIX_DETAILS);

        editMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_RATING,
                PREFIX_STATUS, PREFIX_REFERRAL_STATUS, PREFIX_DETAILS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (editMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(editMultimap.getValue(PREFIX_NAME).get()));
        }
        if (editMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(editMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (editMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(editMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (editMultimap.getValue(PREFIX_RATING).isPresent()) {
            editPersonDescriptor.setRating(ParserUtil.parseRating(editMultimap.getValue(PREFIX_RATING).get()));
        }
        if (editMultimap.getValue(PREFIX_STATUS).isPresent()) {
            editPersonDescriptor.setStatus(ParserUtil.parseAddress(editMultimap.getValue(PREFIX_STATUS).get()));
        }
        parseRolesForEdit(editMultimap.getAllValues(PREFIX_ROLE)).ifPresent(editPersonDescriptor::setRoles);

        if (editMultimap.getValue(PREFIX_REFERRAL_STATUS).isPresent()) {
            editPersonDescriptor.setReferralStatus(ParserUtil.parseReferralStatus(editMultimap
                    .getValue(PREFIX_REFERRAL_STATUS).get()));
        }

        if (editMultimap.getValue(PREFIX_DETAILS).isPresent()) {
            editPersonDescriptor.setDetails(ParserUtil.parseDetail(editMultimap.getValue(PREFIX_DETAILS).get()));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(BatchEditCommand.MESSAGE_NOT_EDITED);
        }

        return new BatchEditCommand(predicate, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> roles} into a {@code Set<Role>} if {@code roles} is non-empty.
     * If {@code roles} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Role>} containing zero roles.
     */
    private Optional<Set<Role>> parseRolesForEdit(Collection<String> roles) throws ParseException {
        assert roles != null;

        if (roles.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> roleSet = roles.size() == 1 && roles.contains("") ? Collections.emptySet() : roles;
        return Optional.of(ParserUtil.parseRoles(roleSet));
    }
}
