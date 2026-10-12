/**
 * Shared helper methods that check user input before objects are created or changed.
 *
 * Responsibility: validation only. It holds no data, so every method is static and the
 * class cannot be instantiated (private constructor). This prevents the same checks
 * from being copied into every domain class (avoids code duplication).
 *
 * Used by: Employee and its subclasses, Task, Document, Training, WorkflowStage subclasses,
 * HROnboardingSystem, and HROnboardingApp.
 *
 * Error handling: each method throws IllegalArgumentException when input is invalid.
 * The UI layer catches this exception and shows the message to the user.
 */
public final class ValidationUtil {

    /** Private constructor: prevents creating objects of this helper class. */
    private ValidationUtil() {
    }

    /**
     * Checks that a text value is not null and not blank.
     *
     * @param value     the text to check
     * @param fieldName the name shown in the error message (for example, "Name")
     * @return the value with leading and trailing spaces removed
     * @throws IllegalArgumentException if value is null or contains only spaces
     */
    public static String requireText(String value, String fieldName) {
        // isBlank() is true for empty or whitespace-only text; the null check above handles null
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    /**
     * Checks that a whole number is inside an inclusive range.
     *
     * @param value     the number to check
     * @param min       the smallest allowed value
     * @param max       the largest allowed value
     * @param fieldName the name shown in the error message
     * @return the same value, if valid
     * @throws IllegalArgumentException if value is outside [min, max]
     */
    public static int requireInRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ".");
        }
        return value;
    }

    /**
     * Checks a simple email format: one '@' that is not the first or last character,
     * with no second '@' after it. This is a basic check, not a full RFC 5322 validator.
     *
     * @param value the email text to check
     * @return the trimmed email
     * @throws IllegalArgumentException if the text is empty or does not match the simple format
     */
    public static String requireEmail(String value) {
        String email = requireText(value, "Email");
        int atIndex = email.indexOf('@');
        // Conditions: '@' must exist (index > -1), must not be the first character (> 0),
        // must not be the last character (< length - 1), and must appear only once.
        boolean valid = atIndex > 0 && atIndex < email.length() - 1 && email.indexOf('@', atIndex + 1) == -1;
        if (!valid) {
            throw new IllegalArgumentException("Email must look like name@company.com.");
        }
        return email;
    }
}
