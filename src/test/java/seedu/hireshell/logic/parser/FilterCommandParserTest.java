package seedu.hireshell.logic.parser;

import static seedu.hireshell.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.hireshell.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.hireshell.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.hireshell.logic.commands.FilterCommand;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.DateFilter;
import seedu.hireshell.model.person.PersonMatchesFiltersPredicate.RatingFilter;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // rating only, various operators
        assertParseSuccess(parser, " " + PREFIX_RATING + ">= 7",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.GREATER_THAN_OR_EQUAL, 7.0), null, null, null)));

        assertParseSuccess(parser, " " + PREFIX_RATING + "> 7",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.GREATER_THAN, 7.0), null, null, null)));

        assertParseSuccess(parser, " " + PREFIX_RATING + "< 5",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.LESS_THAN, 5.0), null, null, null)));

        assertParseSuccess(parser, " " + PREFIX_RATING + "<= 5",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.LESS_THAN_OR_EQUAL, 5.0), null, null, null)));

        assertParseSuccess(parser, " " + PREFIX_RATING + "== 8.5",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.EQUAL, 8.5), null, null, null)));

        // default operator (equal)
        assertParseSuccess(parser, " " + PREFIX_RATING + " 8.5",
                new FilterCommand(new PersonMatchesFiltersPredicate(
                        new RatingFilter(RatingFilter.Operator.EQUAL, 8.5), null, null, null)));

        // status only
        assertParseSuccess(parser, " " + PREFIX_STATUS + "Interviewing",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, "Interviewing", null, null)));

        // date only
        assertParseSuccess(parser, " " + PREFIX_DATE + "before 2026-04-01",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, null,
                        new DateFilter(DateFilter.Operator.BEFORE, LocalDate.of(2026, 4, 1)), null)));

        assertParseSuccess(parser, " " + PREFIX_DATE + "after 2026-04-01",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, null,
                        new DateFilter(DateFilter.Operator.AFTER, LocalDate.of(2026, 4, 1)), null)));

        assertParseSuccess(parser, " " + PREFIX_DATE + "on 2026-04-01",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, null,
                        new DateFilter(DateFilter.Operator.EQUAL, LocalDate.of(2026, 4, 1)), null)));

        // default date operator (on)
        assertParseSuccess(parser, " " + PREFIX_DATE + " 2026-04-01",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, null,
                        new DateFilter(DateFilter.Operator.EQUAL, LocalDate.of(2026, 4, 1)), null)));

        // role only
        assertParseSuccess(parser, " " + PREFIX_ROLE + "Software Engineer",
                new FilterCommand(new PersonMatchesFiltersPredicate(null, null, null, "Software Engineer")));

        // both rating and status
        PersonMatchesFiltersPredicate predicateBoth = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.LESS_THAN, 5.5),
                "Rejected", null, null);
        FilterCommand expectedFilterCommandBoth = new FilterCommand(predicateBoth);
        assertParseSuccess(parser, " " + PREFIX_RATING + "< 5.5 " + PREFIX_STATUS + "Rejected",
                expectedFilterCommandBoth);

        // all filters
        PersonMatchesFiltersPredicate predicateAll = new PersonMatchesFiltersPredicate(
                new RatingFilter(RatingFilter.Operator.GREATER_THAN_OR_EQUAL, 8.0),
                "Accepted",
                new DateFilter(DateFilter.Operator.AFTER, LocalDate.of(2026, 1, 1)),
                "Software Engineer");
        assertParseSuccess(parser, " " + PREFIX_RATING + ">= 8 " + PREFIX_STATUS + "Accepted "
                + PREFIX_DATE + "after 2026-01-01 " + PREFIX_ROLE + "Software Engineer",
                new FilterCommand(predicateAll));
    }

    @Test
    public void parse_invalidRating_throwsParseException() {
        // invalid rating value
        assertParseFailure(parser, " " + PREFIX_RATING + ">= abc",
                "Ratings should be a number between 0 and 10 (decimals allowed).");

        // rating value out of range
        assertParseFailure(parser, " " + PREFIX_RATING + " 11",
                "Ratings should be a number between 0 and 10 (decimals allowed).");
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        // missing value
        assertParseFailure(parser, " " + PREFIX_DATE + "before ",
                "Date value cannot be empty. Format: [before/after/on] DATE (YYYY-MM-DD)");

        // invalid date format
        assertParseFailure(parser, " " + PREFIX_DATE + "before 01-04-2026",
                "Invalid date format! Use YYYY-MM-DD");

        // invalid operator
        assertParseFailure(parser, " " + PREFIX_DATE + "between 2026-04-01",
                "Date filter format is: [before/after/on] DATE (YYYY-MM-DD)");
    }

    @Test
    public void parse_multiplePrefixes_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_RATING + ">= 5 " + PREFIX_RATING + "< 8",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_RATING);
        assertParseFailure(parser, " " + PREFIX_DATE + "before 2026-01-01 " + PREFIX_DATE + "after 2026-01-01",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_DATE);
        assertParseFailure(parser, " " + PREFIX_ROLE + "Software Engineer " + PREFIX_ROLE + "Analyst",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_ROLE);
    }
}
