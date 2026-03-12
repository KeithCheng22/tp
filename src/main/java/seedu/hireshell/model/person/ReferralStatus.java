package seedu.hireshell.model.person;

/**
 * Represents the referral status of a {@code Person}.
 * A person can either be referred or not referred.
 */
public enum ReferralStatus {
    REFERRED,
    NOT_REFERRED, DEFAULT_REFERRAL_STATUS;

    /**
     * Message to show when the user provides an invalid referral status.
     */
    public static final String MESSAGE_CONSTRAINTS = "Referral Status must be 'yes' or 'no', "
            + "and it should not be blank";

    /**
     * Returns true if the given string is a valid referral status.
     * A valid referral status must be either "yes" or "no" (case-insensitive).
     *
     * @param referralStatus The string to validate.
     * @return {@code true} if the input is a valid referral status, {@code false} otherwise.
     */
    public static boolean isValidReferralStatus(String referralStatus) {
        String trimmedReferralStatus = referralStatus.trim().toLowerCase();
        return trimmedReferralStatus.equals("yes") || trimmedReferralStatus.equals("no");
    }

    /**
     * Converts a string representation of referral status into the corresponding {@code ReferralStatus}.
     * The input is expected to be validated beforehand using {@link #isValidReferralStatus(String)}.
     *
     * @param referralStatus A string representing the referral status ("yes" or "no").
     * @return {@code REFERRED} if the input is "yes", otherwise {@code NOT_REFERRED}.
     */
    public static ReferralStatus fromString(String referralStatus) {
        String trimmedReferralStatus = referralStatus.trim().toLowerCase();
        return trimmedReferralStatus.equals("yes") ? REFERRED : NOT_REFERRED;
    }

    /**
     * Returns the user-friendly string representation of the referral status.
     * Used when displaying the referral status in the UI.
     *
     * @return "Yes" if {@code REFERRED}, "No" if {@code NOT_REFERRED}.
     */
    @Override
    public String toString() {
        return switch (this) {
            // CHECKSTYLE.OFF: Indentation
            case REFERRED -> "Yes";
            case NOT_REFERRED -> "No";
            default -> "";
        };
        // CHECKSTYLE.ON: Indentation
    }

}
