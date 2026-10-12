/**
 * Workflow stage for account and role setup. Inherits from WorkflowStage.
 *
 * Implementation assumption (not shown in the UML): this stage owns one Task object.
 */
public class RoleStage extends WorkflowStage {

    /** The setup task this stage must assign and complete. Private. */
    private Task task;

    /**
     * Creates a role-setup stage.
     *
     * @param stageName the readable stage name
     * @param task      the task to process; cannot be null
     * @throws IllegalArgumentException if the task is null
     */
    public RoleStage(String stageName, Task task) {
        super(stageName);
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        this.task = task;
    }

    /**
     * Overrides executeStage(). Assigns and completes the task as needed.
     * Finished steps are skipped, so calling it again is safe.
     */
    @Override
    public void executeStage() {
        if (completed) {
            return;
        }
        // Assign only if the task has not been assigned yet
        if (task.getStatus().equals(Task.NOT_ASSIGNED)) {
            task.assignTask();
        }
        if (!task.isCompleted()) {
            task.completeTask();
        }
        markCompleted();
    }

    /** @return the task this stage processes. */
    public Task getTask() {
        return task;
    }
}
