package seedu.hireshell.ui;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import seedu.hireshell.commons.core.LogsCenter;
import seedu.hireshell.model.person.Person;

/**
 * Panel containing the list of persons displayed as a spreadsheet/table.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> idColumn;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> roleColumn;
    @FXML
    private TableColumn<Person, String> ratingColumn;
    @FXML
    private TableColumn<Person, String> statusColumn;
    @FXML
    private TableColumn<Person, String> contactColumn;
    @FXML
    private TableColumn<Person, String> emailColumn;
    @FXML
    private TableColumn<Person, String> referralColumn;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        setupTable();
        personTableView.setItems(personList);
    }

    /**
     * Configures the TableColumns to extract data from the Person objects.
     */
    private void setupTable() {
        // 1. ID Column
        idColumn.setCellValueFactory(cellData -> {
            int index = personTableView.getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyStringWrapper(String.valueOf(index));
        });

        // 2. Name Column
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getName().toString()));

        // 3. Role Column
        roleColumn.setCellValueFactory(cellData -> {
            String tagsString = cellData.getValue().getRoles().stream()
                    .map(role -> role.roleName)
                    .collect(Collectors.joining(", "));
            return new ReadOnlyStringWrapper(tagsString.isEmpty() ? "-" : tagsString);
        });

        // 4. Rating Column
        ratingColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getRating().toString()));

        // 5. Status Column
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatus().toString()));

        // 6. Contact Column
        contactColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getPhone().toString()));

        // 7. Email Column
        emailColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getEmail().toString()));

        // 8. Referral Column
        referralColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getReferralStatus().toString()));
    }
}