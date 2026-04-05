package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.hireshell.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.hireshell.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.BatchPredicate;
import seedu.hireshell.model.person.Details;
import seedu.hireshell.model.person.Email;
import seedu.hireshell.model.person.Name;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.Phone;
import seedu.hireshell.model.person.Rating;
import seedu.hireshell.model.person.ReferralStatus;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.role.Role;

/**
 * Edits multiple persons identified using matching predicates.
 */
public class BatchEditCommand extends Command {

    public static final String COMMAND_WORD = "batch edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits all persons whose attributes match the specified condition(s).\n"
            + "Parameters: [s/STATUS] [r/ROLE]... [rt/RATING_CONDITION] [dt/DATE_CONDITION] to [EDITS]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "APPLIED " + PREFIX_DATE + "before 2024-01-01 to "
            + PREFIX_STATUS + "REJECTED";

    public static final String MESSAGE_BATCH_EDIT_SUCCESS = "Edited %1$d person(s)";
    public static final String MESSAGE_NO_PERSONS_MATCHED = "No persons match the specified criteria";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "Batch edit resulted in a duplicate person in the address book.";

    private final BatchPredicate predicate;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param predicate condition to filter persons to edit
     * @param editPersonDescriptor details to edit the persons with
     */
    public BatchEditCommand(BatchPredicate predicate, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(predicate);
        requireNonNull(editPersonDescriptor);

        this.predicate = predicate;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> personsToEdit = new ArrayList<>(model.getFilteredPersonList());

        if (personsToEdit.isEmpty()) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            throw new CommandException(MESSAGE_NO_PERSONS_MATCHED);
        }

        for (Person personToEdit : personsToEdit) {
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

            if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setPerson(personToEdit, editedPerson);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_BATCH_EDIT_SUCCESS, personsToEdit.size()));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Rating updatedRating = editPersonDescriptor.getRating().orElse(personToEdit.getRating());
        Status updatedStatus = editPersonDescriptor.getStatus().orElse(personToEdit.getStatus());
        Set<Role> updatedRoles = editPersonDescriptor.getRoles().orElse(personToEdit.getRoles());
        ReferralStatus updatedReferralStatus = editPersonDescriptor.getReferralStatus()
                .orElse(personToEdit.getReferralStatus());
        Details updatedDetails = editPersonDescriptor.getDetails().orElse(personToEdit.getDetails());
        LocalDateTime createdAt = personToEdit.getCreatedAt();

        return new Person(updatedName, updatedPhone, updatedEmail, updatedRating, updatedStatus, updatedRoles,
                updatedReferralStatus, updatedDetails, createdAt);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BatchEditCommand)) {
            return false;
        }

        BatchEditCommand otherBatchEditCommand = (BatchEditCommand) other;
        return predicate.equals(otherBatchEditCommand.predicate)
                && editPersonDescriptor.equals(otherBatchEditCommand.editPersonDescriptor);
    }
}
