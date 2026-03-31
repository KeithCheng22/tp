package seedu.hireshell.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.hireshell.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.hireshell.testutil.Assert.assertThrows;
import static seedu.hireshell.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.hireshell.commons.exceptions.IllegalValueException;
import seedu.hireshell.model.person.Details;
import seedu.hireshell.model.person.Email;
import seedu.hireshell.model.person.Name;
import seedu.hireshell.model.person.Phone;
import seedu.hireshell.model.person.Rating;
import seedu.hireshell.model.person.ReferralStatus;
import seedu.hireshell.model.person.Status;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_RATING = "11.5";
    private static final String INVALID_STATUS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_ROLE = "#friend";
    private static final String INVALID_REFERRAL_STATUS = "Maybe";
    private static final String INVALID_DETAIL = "\\<bad\\detail>";


    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_RATING = BENSON.getRating().toString();
    private static final String VALID_STATUS = BENSON.getStatus().toString();
    private static final List<JsonAdaptedRole> VALID_ROLES = BENSON.getRoles().stream()
            .map(JsonAdaptedRole::new)
            .collect(Collectors.toList());
    private static final String VALID_REFERRAL_STATUS = BENSON.getReferralStatus().toString();
    private static final String VALID_DETAIL = "Met at career fair";
    private static final String VALID_DATETIME = "2026-03-31T12:34:10.221401300";

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE,
                VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES, VALID_REFERRAL_STATUS, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null,
                VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES, VALID_REFERRAL_STATUS, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                null, VALID_RATING, VALID_STATUS, VALID_ROLES, VALID_REFERRAL_STATUS, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRating_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_RATING, VALID_STATUS, VALID_ROLES,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = Rating.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullRating_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_EMAIL, null, VALID_STATUS, VALID_ROLES, VALID_REFERRAL_STATUS, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Rating.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_RATING, INVALID_STATUS, VALID_ROLES,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = Status.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_RATING, null, VALID_ROLES, VALID_REFERRAL_STATUS, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRoles_throwsIllegalValueException() {
        List<JsonAdaptedRole> invalidRoles = new ArrayList<>(VALID_ROLES);
        invalidRoles.add(new JsonAdaptedRole(INVALID_ROLE));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_RATING, VALID_STATUS, invalidRoles,
                        VALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidReferralStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES,
                        INVALID_REFERRAL_STATUS, VALID_DETAIL, VALID_DATETIME);
        String expectedMessage = ReferralStatus.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullReferralStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES, null, VALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, ReferralStatus.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidDetail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_RATING, VALID_STATUS, VALID_ROLES, VALID_REFERRAL_STATUS, INVALID_DETAIL,
                VALID_DATETIME);
        String expectedMessage = Details.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
