package seedu.hireshell.ui;

import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.binding.NumberBinding;
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
    @FXML
    private TableColumn<Person, String> dateColumn;

    private final BiConsumer<Person, Integer> onPersonSelected;
    private boolean isSyncingSelection;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, BiConsumer<Person, Integer> onPersonSelected) {
        super(FXML);
        this.onPersonSelected = onPersonSelected;
        setupTable();
        personTableView.setItems(personList);

        personTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (isSyncingSelection) {
                return;
            }

            int selectedIndex = personTableView.getSelectionModel().getSelectedIndex() + 1;
            logger.info("User clicked on candidate index: " + selectedIndex);

            // Guard against transient deselection events during table refresh/edit.
            if (newValue == null || selectedIndex <= 0) {
                return;
            }

            if (this.onPersonSelected != null) {
                this.onPersonSelected.accept(newValue, selectedIndex);
            }
        });
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

        dateColumn.setCellValueFactory(cellData -> // 9. Date Column
                new ReadOnlyStringWrapper(cellData.getValue().getCreatedAt().toLocalDate().toString()));

        personTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        // Setting the widths of each column
        NumberBinding dynamicColumnWidth = personTableView.widthProperty()
                .subtract(idColumn.widthProperty())
                .subtract(ratingColumn.widthProperty())
                .subtract(contactColumn.widthProperty())
                .subtract(referralColumn.widthProperty())
                .subtract(dateColumn.widthProperty())
                .subtract(30)
                .divide(4);

        nameColumn.prefWidthProperty().bind(dynamicColumnWidth);
        roleColumn.prefWidthProperty().bind(dynamicColumnWidth);
        statusColumn.prefWidthProperty().bind(dynamicColumnWidth);
        emailColumn.prefWidthProperty().bind(dynamicColumnWidth);
    }

    /**
     * Synchronizes the table row selection with the current model-selected person.
     * @param person the person that should be selected in the table, or {@code null} to clear selection
     */
    public void syncSelectionFromModel(Person person) {
        isSyncingSelection = true;
        try {
            if (person == null) {
                personTableView.getSelectionModel().clearSelection();
                return;
            }

            // If person is not visible in current filtered list, clear selection.
            if (!personTableView.getItems().contains(person)) {
                personTableView.getSelectionModel().clearSelection();
                return;
            }

            personTableView.getSelectionModel().select(person);
            personTableView.scrollTo(person);
        } finally {
            isSyncingSelection = false;
        }
    }
}
