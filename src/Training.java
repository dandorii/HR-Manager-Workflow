import java.time.LocalDate;

/**
 * A training session an employee must attend during onboarding (for example, company orientation).
 *
 * Lifecycle: NOT_SCHEDULED -> SCHEDULED -> COMPLETED. Methods enforce this order.
 *
 * Not a subclass of anything. It is used by TrainingStage, which owns one Training object.
 */
public class Training {

    // Status constants shared with other classes (for example, TrainingStage checks NOT_SCHEDULED)
    public static final String NOT_SCHEDULED = "Not Scheduled";
    public static final String SCHEDULED = "Scheduled";
    public static final String COMPLETED = "Completed";

    private String trainingId;    // unique ID
    private String trainingName;  // readable name (for example, "Company Orientation")
    private String trainer;       // who leads the session (UML attribute "trainer")
    private String schedule;      // date text, or "Not set" until scheduled
    private String status;        // one of the three constants above

    /**
     * Creates a training record that is not yet scheduled.
     *
     * @param trainingId   the unique ID; cannot be blank
     * @param trainingName the readable name; cannot be blank
     * @param trainer      the person or team leading the session; cannot be blank
     * @throws IllegalArgumentException if a value is blank
     */
    public Training(String trainingId, String trainingName, String trainer) {
        this.trainingId = ValidationUtil.requireText(trainingId, "Training ID");
        this.trainingName = ValidationUtil.requireText(trainingName, "Training name");
        this.trainer = ValidationUtil.requireText(trainer, "Trainer");
        this.schedule = "Not set";
        this.status = NOT_SCHEDULED;
    }

    /**
     * Schedules the training and moves the status to SCHEDULED. Only allowed from NOT_SCHEDULED.
     * The UML shows no parameters, so the date is set here: one week from today.
     *
     * @throws IllegalStateException if the training is already scheduled or completed
     */
    public void scheduleTraining() {
        // Blocks scheduling again, which would overwrite an existing date
        if (!status.equals(NOT_SCHEDULED)) {
            throw new IllegalStateException("Training '" + trainingName + "' is already scheduled or completed.");
        }
        // Calculation: today's date plus 7 days, converted to text (for example, "2026-10-19")
        this.schedule = LocalDate.now().plusDays(7).toString();
        this.status = SCHEDULED;
    }

    /**
     * Marks the training as completed. Only allowed after it has been scheduled.
     *
     * @throws IllegalStateException if the training is not scheduled yet
     */
    public void completeTraining() {
        if (!status.equals(SCHEDULED)) {
            throw new IllegalStateException("Training must be scheduled before it can be completed.");
        }
        this.status = COMPLETED;
    }

    /** @return true if the status is COMPLETED. */
    public boolean isCompleted() {
        return status.equals(COMPLETED);
    }

    /** @return the training ID. */
    public String getTrainingId() {
        return trainingId;
    }

    /** @return the training name. */
    public String getTrainingName() {
        return trainingName;
    }

    /** @return the trainer's name. */
    public String getTrainer() {
        return trainer;
    }

    /** @return the schedule text. */
    public String getSchedule() {
        return schedule;
    }

    /** @return the current status text. */
    public String getStatus() {
        return status;
    }
}
