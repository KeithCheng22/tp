package seedu.hireshell.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.hireshell.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.hireshell.model.Model;
import seedu.hireshell.model.ModelManager;
import seedu.hireshell.model.UserPrefs;

/**
 * Contains integration tests for {@code ExportCommand}.
 */
public class ExportCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_exportSuccessful_createsCsvFile() throws Exception {
        Path dataPath = tempDir.resolve("").resolve("hireshell.json");
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(dataPath);

        Model model = new ModelManager(getTypicalAddressBook(), userPrefs);
        ExportCommand exportCommand = new ExportCommand();

        CommandResult result = exportCommand.execute(model);

        Path expectedCsvPath = Path.of("").toAbsolutePath().resolve(ExportCommand.CSV_FILE_NAME);
        String csvContent = Files.readString(expectedCsvPath);

        assertTrue(Files.exists(expectedCsvPath));
        assertTrue(csvContent.startsWith("Name,Phone,Email,Rating,Status,Referral Status,Roles"));
        assertTrue(csvContent.contains("\"Alice Pauline\""));
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, model.getAddressBook().getPersonList().size(),
                expectedCsvPath.toAbsolutePath()), result.getFeedbackToUser());
    }
}
