/**
 * Workflow stage for orientation and training. Inherits from WorkflowStage.
 *
 * Implementation assumption (not shown in the UML): this stage owns one Training object.
 */
public class TrainingStage extends WorkflowStage {

    /** The training session this stage must schedule and complete. Private. */
    private Training training;

    /**
     * Creates a training stage.
     *
     * @param stageName the readable stage name
     * @param training  the training to process; cannot be null
     * @throws IllegalArgumentException if the training is null
     */
    public TrainingStage(String stageName, Training training) {
        super(stageName);
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null.");
        }
        this.training = training;
    }

    /**
     * Overrides executeStage(). Schedules the training if needed, then completes it.
     * Safe to call again because finished steps are skipped.
     */
    @Override
    public void executeStage() {
        if (completed) {
            return;
        }
        // Schedule only if no date has been set yet (Training sets the date itself)
        if (training.getStatus().equals(Training.NOT_SCHEDULED)) {
            training.scheduleTraining();
        }
        if (!training.isCompleted()) {
            training.completeTraining();
        }
        markCompleted();
    }

    /** @return the training this stage processes. */
    public Training getTraining() {
        return training;
    }
}
