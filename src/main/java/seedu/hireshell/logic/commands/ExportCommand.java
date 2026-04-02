package seedu.hireshell.logic.commands;

import static java.util.Comparator.naturalOrder;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.Person;

/**
 * Exports all persons in the address book to a csv file.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all persons in the address book to a CSV file.\n"
            + "Example: " + COMMAND_WORD;

    public static final String CSV_FILE_NAME = "hireshell_export.csv";
    public static final String MESSAGE_SUCCESS = "Exported %1$d persons to %2$s";
    public static final String MESSAGE_EXPORT_FAILED = "Failed to export data to CSV: %1$s";

    private static final String CSV_HEADER = "Name,Phone,Email,Rating,Status,Referral Status,Roles,Details,CreatedAt";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> persons = model.getAddressBook().getPersonList();
        //Uses Current Working Directory for save file path
        Path csvPath = Path.of("").toAbsolutePath().resolve(CSV_FILE_NAME);

        try {
            Path parentDirectory = csvPath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            String csvContent = buildCsvContent(persons);
            Files.writeString(csvPath, csvContent, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException exception) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILED, exception.getMessage()));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, persons.size(), csvPath.toAbsolutePath()));
    }

    private String buildCsvContent(List<Person> persons) {
        StringBuilder csvBuilder = new StringBuilder(CSV_HEADER).append(System.lineSeparator());

        for (Person person : persons) {
            csvBuilder.append(buildCsvRow(person)).append(System.lineSeparator());
        }

        return csvBuilder.toString();
    }

    //Add additional fields here
    private String buildCsvRow(Person person) {
        String roles = person.getRoles().stream()
                .map(role -> role.roleName)
                .sorted(naturalOrder())
                .collect(Collectors.joining(";"));

        return String.join(",",
                escapeCsv(person.getName().toString()),
                escapeCsv(person.getPhone().toString()),
                escapeCsv(person.getEmail().toString()),
                escapeCsv(person.getRating().toString()),
                escapeCsv(person.getStatus().toString()),
                escapeCsv(person.getReferralStatus().toString()),
                escapeCsv(roles),
                escapeCsv(person.getDetails().toString()),
                escapeCsv(person.getCreatedAt().toString()));
    }

    private String escapeCsv(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

}
