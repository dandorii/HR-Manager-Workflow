import java.time.LocalDate;

/**
 * A required onboarding document (for example, a signed contract or ID copy).
 *
 * Lifecycle (status moves in one direction only):
 *   PENDING -> SUBMITTED -> VERIFIED
 * Each step is checked, so the methods cannot be called out of order.
 *
 * OOP concepts shown here:
 * - Compile-time polymorphism (overloading): two submitDocument methods with different parameters.
 * - Encapsulation: all fields are private; the status can only change through these methods.
 */
public class Document {

    // Status constants. public static final: shared fixed values, so callers can compare
    // against Document.PENDING instead of retyping the text.
    public static final String PENDING = "Pending";
    public static final String SUBMITTED = "Submitted";
    public static final String VERIFIED = "Verified";

    private String documentId;      // unique ID for this document
    private String documentName;    // readable name (for example, "Employee Documents")
    private String status;          // current status: one of the three constants above
    private String submissionDate;  // date text, or "Not submitted" before submission

    /**
     * Creates a document in PENDING status.
     *
     * @param documentId   the unique ID; cannot be blank
     * @param documentName the readable name; cannot be blank
     * @throws IllegalArgumentException if a value is blank
     */
    public Document(String documentId, String documentName) {
        this.documentId = ValidationUtil.requireText(documentId, "Document ID");
        this.documentName = ValidationUtil.requireText(documentName, "Document name");
        this.status = PENDING;               // starting state
        this.submissionDate = "Not submitted";
    }

    /**
     * Submits the document using today's date. This is the overloaded version with no
     * parameters (compile-time polymorphism). It delegates to the version below.
     */
    public void submitDocument() {
        submitDocument(LocalDate.now().toString());
    }

    /**
     * Submits the document with a date supplied by the caller.
     * Only allowed while the status is PENDING.
     *
     * @param submissionDate the submission date text; cannot be blank
     * @throws IllegalStateException    if the document was already submitted
     * @throws IllegalArgumentException if the date is blank
     */
    public void submitDocument(String submissionDate) {
        // State check: prevents submitting twice (wrong order of operations)
        if (!status.equals(PENDING)) {
            throw new IllegalStateException("Document '" + documentName + "' has already been submitted.");
        }
        this.submissionDate = ValidationUtil.requireText(submissionDate, "Submission date");
        this.status = SUBMITTED;
    }

    /**
     * Marks a submitted document as verified (checked by HR).
     *
     * @throws IllegalStateException if the document has not been submitted yet
     */
    public void verifyDocument() {
        // Cannot verify something that has not been submitted
        if (!status.equals(SUBMITTED)) {
            throw new IllegalStateException("Document must be submitted before it can be verified.");
        }
        this.status = VERIFIED;
    }

    /** @return true if the status is VERIFIED. Used by DocumentStage to decide what is left to do. */
    public boolean isVerified() {
        return status.equals(VERIFIED);
    }

    /** @return the document ID. */
    public String getDocumentId() {
        return documentId;
    }

    /** @return the document name. */
    public String getDocumentName() {
        return documentName;
    }

    /** @return the current status text. */
    public String getStatus() {
        return status;
    }

    /** @return the submission date text. */
    public String getSubmissionDate() {
        return submissionDate;
    }
}
