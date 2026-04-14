package seedu.hireshell.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_REFERRAL_STATUS;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.hireshell.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.hireshell.commons.core.index.Index;
import seedu.hireshell.commons.util.CollectionUtil;
import seedu.hireshell.commons.util.ToStringBuilder;
import seedu.hireshell.logic.Messages;
import seedu.hireshell.logic.commands.exceptions.CommandException;
import seedu.hireshell.model.Model;
import seedu.hireshell.model.person.Details;
import seedu.hireshell.model.person.Email;
import seedu.hireshell.model.person.Name;
import seedu.hireshell.model.person.Person;
import seedu.hireshell.model.person.Phone;
import seedu.hireshell.model.person.Rating;
import seedu.hireshell.model.person.ReferralStatus;
import seedu.hireshell.model.person.Status;
import seedu.hireshell.model.person.exceptions.DuplicatePersonException;
import seedu.hireshell.model.role.Role;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_RATING + "RATING] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_REFERRAL_STATUS + "REFERRAL_STATUS] "
            + "[" + PREFIX_ROLE + "ROLE]..."
            + "[" + PREFIX_DETAILS + "DETAILS]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_RATING + "9.0 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        try {
            model.setPerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
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
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Status status;
        private Set<Role> roles;
        private Rating rating;
        private ReferralStatus referralStatus;
        private Details details;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code roles} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setRating(toCopy.rating);
            setStatus(toCopy.status);
            setRoles(toCopy.roles);
            setReferralStatus(toCopy.referralStatus);
            setDetails(toCopy.details);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, rating, status, roles, referralStatus, details);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Optional<Rating> getRating() {
            return Optional.ofNullable(rating);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Optional<Status> getStatus() {
            return Optional.ofNullable(status);
        }

        public void setDetails(Details details) {
            this.details = details;
        }

        public Optional<Details> getDetails() {
            return Optional.ofNullable(details);
        }

        /**
         * Sets {@code roles} to this object's {@code roles}.
         * A defensive copy of {@code roles} is used internally.
         */
        public void setRoles(Set<Role> roles) {
            this.roles = (roles != null) ? new HashSet<>(roles) : null;
        }

        /**
         * Returns an unmodifiable role set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code roles} is null.
         */
        public Optional<Set<Role>> getRoles() {
            return (roles != null) ? Optional.of(Collections.unmodifiableSet(roles)) : Optional.empty();
        }

        public void setReferralStatus(ReferralStatus referralStatus) {
            this.referralStatus = referralStatus;
        }

        public Optional<ReferralStatus> getReferralStatus() {
            return Optional.ofNullable(referralStatus);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(rating, otherEditPersonDescriptor.rating)
                    && Objects.equals(status, otherEditPersonDescriptor.status)
                    && Objects.equals(roles, otherEditPersonDescriptor.roles)
                    && Objects.equals(referralStatus, otherEditPersonDescriptor.referralStatus)
                    && Objects.equals(details, otherEditPersonDescriptor.details);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("rating", rating)
                    .add("status", status)
                    .add("referralStatus", referralStatus)
                    .add("roles", roles)
                    .add("details", details)
                    .toString();
        }
    }
}
