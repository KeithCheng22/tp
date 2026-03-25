package seedu.hireshell.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.hireshell.commons.core.LogsCenter;
import seedu.hireshell.logic.commands.CommandResult;
import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.logic.parser.exceptions.ParseException;



/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private final List<String> commandHistory = new ArrayList<>();
    private int commandHistoryIndex = -1;
    private String latestCommand = "";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        commandTextField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                // CHECKSTYLE.OFF: Indentation
                case UP -> navigateHistoryUp();
                case DOWN -> navigateHistoryDown();
                default -> {

                }
                // CHECKSTYLE.ON: Indentation
            }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandHistory.add(commandText);
            commandHistoryIndex = -1;
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Navigates one step up in the command history and updates the text field.
     * <p>
     * If the command history is empty, this method does nothing.
     * If this is the first navigation (commandHistoryIndex is -1), it saves the
     * current text in the text field to `latestCommand`.
     * After navigating up, the command at the new index is displayed in the
     * `commandTextField`, and the caret is moved to the end of the text.
     */
    private void navigateHistoryUp() {
        if (commandHistory.isEmpty()) {
            logger.info("navigateHistoryUp: No command history");
            return;
        }

        if (commandHistoryIndex == -1) {
            latestCommand = commandTextField.getText();
            commandHistoryIndex = commandHistory.size();
            logger.info("navigateHistoryUp: First navigation up, saved latest command: " + latestCommand);
        }

        commandHistoryIndex--;
        if (commandHistoryIndex < 0) {
            logger.info("navigateHistoryUp: Reached end of command history");
            commandHistoryIndex = 0;
        }

        commandTextField.setText(commandHistory.get(commandHistoryIndex));
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Navigates one step down in the command history and updates the text field.
     * <p>
     * If the command history is empty or there is no current navigation
     * (commandHistoryIndex is -1), this method does nothing.
     * After navigating down, the command at the new index is displayed in the
     * `commandTextField`.
     * If the end of the history is reached, the original `latestCommand` is restored,
     * and navigation resets (commandHistoryIndex set to -1).
     * The caret is always moved to the end of the text.
     */
    private void navigateHistoryDown() {
        if (commandHistory.isEmpty() || commandHistoryIndex == -1) {
            return;
        }

        commandHistoryIndex++;
        if (commandHistoryIndex >= commandHistory.size()) {
            logger.info("navigateHistoryDown: Reached latest command, restored latest command: " + latestCommand);
            commandTextField.setText(latestCommand);
            assert latestCommand.equals(commandTextField.getText()) : "navigateHistoryDown: commandTextField.getText() "
                    + "should return to the latest command";
            commandHistoryIndex = -1;
        } else {
            commandTextField.setText(commandHistory.get(commandHistoryIndex));
        }

        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.hireshell.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
