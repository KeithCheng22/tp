package seedu.hireshell.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.hireshell.logic.commands.FilterCommand;
import seedu.hireshell.logic.parser.exceptions.ParseException;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.DateFilter;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.RatingFilter;
import seedu.hireshell.model.person.Rating;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Pattern RATING_FILTER_PATTERN = Pattern.compile("(?<operator>[><]=?|==)?\\s*(?<value>.*)");
    private static final Pattern DATE_FILTER_PATTERN = Pattern.compile("(?<operator>before|after|on)\\s+(?<value>.*)");

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @param args The arguments to parse.
     * @return The resulting FilterCommand.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_RATING, PREFIX_STATUS, PREFIX_DATE,
                PREFIX_ROLE);

        if (!argMultimap.getValue(PREFIX_RATING).isPresent() && !argMultimap.getValue(PREFIX_STATUS).isPresent()
                && !argMultimap.getValue(PREFIX_DATE).isPresent() && !argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_RATING, PREFIX_STATUS, PREFIX_DATE, PREFIX_ROLE);

        RatingFilter ratingFilter = null;
        if (argMultimap.getValue(PREFIX_RATING).isPresent()) {
            ratingFilter = parseRatingFilter(argMultimap.getValue(PREFIX_RATING).get());
        }

        String statusFilter = argMultimap.getValue(PREFIX_STATUS).orElse(null);

        DateFilter dateFilter = null;
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            dateFilter = parseDateFilter(argMultimap.getValue(PREFIX_DATE).get());
        }

        String roleFilter = argMultimap.getValue(PREFIX_ROLE).orElse(null);

        return new FilterCommand(new PersonMatchesFiltersPredicate(ratingFilter, statusFilter, dateFilter, roleFilter));
    }

    /**
     * Parses the rating filter argument.
     */
    private RatingFilter parseRatingFilter(String ratingArg) throws ParseException {
        Matcher matcher = RATING_FILTER_PATTERN.matcher(ratingArg.trim());
        if (!matcher.matches()) {
            throw new ParseException(Rating.MESSAGE_CONSTRAINTS);
        }

        String operatorStr = matcher.group("operator");
        String valueStr = matcher.group("value");

        if (!Rating.isValidRating(valueStr)) {
            throw new ParseException(Rating.MESSAGE_CONSTRAINTS);
        }

        double value = Double.parseDouble(valueStr);
        RatingFilter.Operator operator = parseOperator(operatorStr);

        return new RatingFilter(operator, value);
    }

    /**
     * Parses the date filter argument.
     */
    private DateFilter parseDateFilter(String dateArg) throws ParseException {
        String trimmedDateArg = dateArg.trim().toLowerCase();
        // Try matching operator first
        Matcher matcher = DATE_FILTER_PATTERN.matcher(trimmedDateArg);
        if (matcher.matches()) {
            String operatorStr = matcher.group("operator");
            String valueStr = matcher.group("value").trim();
            if (valueStr.isEmpty()) {
                throw new ParseException("Date value cannot be empty. Format: [before/after/on] DATE (YYYY-MM-DD)");
            }
            LocalDate date;
            try {
                date = LocalDate.parse(valueStr);
            } catch (DateTimeParseException e) {
                throw new ParseException("Invalid date format! Use YYYY-MM-DD");
            }
            DateFilter.Operator operator = operatorStr.equals("before")
                    ? DateFilter.Operator.BEFORE : operatorStr.equals("after")
                    ? DateFilter.Operator.AFTER : DateFilter.Operator.EQUAL;
            return new DateFilter(operator, date);
        }

        // Check if it's just a date without operator (default to EQUAL)
        try {
            LocalDate date = LocalDate.parse(trimmedDateArg);
            return new DateFilter(DateFilter.Operator.EQUAL, date);
        } catch (DateTimeParseException e) {
            // Check if it starts with an operator but has no value
            for (String op : new String[]{"before", "after", "on"}) {
                if (trimmedDateArg.equals(op) || trimmedDateArg.startsWith(op + " ")) {
                    throw new ParseException("Date value cannot be empty. Format: [before/after/on] DATE (YYYY-MM-DD)");
                }
            }
            throw new ParseException("Date filter format is: [before/after/on] DATE (YYYY-MM-DD)");
        }
    }

    /**
     * Parses the operator string into a {@code RatingFilter.Operator}.
     */
    private RatingFilter.Operator parseOperator(String operatorStr) {
        if (operatorStr == null || operatorStr.equals("==")) {
            return RatingFilter.Operator.EQUAL;
        }
        switch (operatorStr) {
        case ">":
            return RatingFilter.Operator.GREATER_THAN;
        case ">=":
            return RatingFilter.Operator.GREATER_THAN_OR_EQUAL;
        case "<":
            return RatingFilter.Operator.LESS_THAN;
        case "<=":
            return RatingFilter.Operator.LESS_THAN_OR_EQUAL;
        default:
            return RatingFilter.Operator.EQUAL;
        }
    }
}
