/**
 * Workflow stage for document submission. Inherits from WorkflowStage.
 *
 * Implementation assumption (not shown in the UML): this stage owns one Document object.
 * It is not a confirmed UML relationship.
 */
public class DocumentStage extends WorkflowStage {

    /** The document this stage must get submitted and verified. Private. */
    private Document document;

    /**
     * Creates a document stage.
     *
     * @param stageName the readable stage name
     * @param document  the document to process; cannot be null
     * @throws IllegalArgumentException if the document is null
     */
    public DocumentStage(String stageName, Document document) {
        super(stageName);   // initializes stageName and completed in the parent
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null.");
        }
        this.document = document;
    }

    /**
     * Overrides executeStage(). Submits and verifies the document as needed.
     * Only the steps that are not yet done are performed, so calling it twice is safe.
     */
    @Override
    public void executeStage() {
        // Stop early if this stage has already finished
        if (completed) {
            return;
        }
        // Each step runs only if the document has not reached it yet
        if (!document.isVerified()) {
            if (document.getStatus().equals(Document.PENDING)) {
                document.submitDocument();   // overloaded no-argument version (uses today's date)
            }
            document.verifyDocument();
        }
        markCompleted();   // inherited from WorkflowStage
    }

    /** @return the document this stage processes. */
    public Document getDocument() {
        return document;
    }
}
