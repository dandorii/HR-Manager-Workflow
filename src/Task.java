/**
 * A role-setup task (for example, creating accounts and assigning a role).
 *
 * Lifecycle: NOT_ASSIGNED -> ASSIGNED -> COMPLETED, enforced by the methods below.
 *
 * Not a subclass of anything. It is used by RoleStage, which owns one Task object.
 */
public class Task {

    // Status constants used by Task and by RoleStage
    public static final String NOT_ASSIGNED = "Not Assigned";
    public static final String ASSIGNED = "Assigned";
    public static final String COMPLETED = "Completed";

    private String taskId;        // unique ID
    private String taskName;      // short name
    private String description;   // what must be done
    private String dueDate;       // due date text
    private String status;        // one of the three constants above
    private String department;    // department responsible for the task

    /**
     * Creates a task that is not yet assigned.
     *
     * @param taskId      the unique ID; cannot be blank
     * @param taskName    the short name; cannot be blank
     * @param description what must be done; cannot be blank
     * @param dueDate     the due date text; cannot be blank
     * @param department  the responsible department; cannot be blank
     * @throws IllegalArgumentException if a value is blank
     */
    public Task(String taskId, String taskName, String description, String dueDate, String department) {
        this.taskId = ValidationUtil.requireText(taskId, "Task ID");
        this.taskName = ValidationUtil.requireText(taskName, "Task name");
        this.description = ValidationUtil.requireText(description, "Description");
        this.dueDate = ValidationUtil.requireText(dueDate, "Due date");
        this.department = ValidationUtil.requireText(department, "Department");
        this.status = NOT_ASSIGNED;   // starting state
    }

    /**
     * Assigns the task. Only allowed from NOT_ASSIGNED.
     *
     * @throws IllegalStateException if the task is already assigned or completed
     */
    public void assignTask() {
        if (!status.equals(NOT_ASSIGNED)) {
            // toLowerCase() makes the message read naturally, for example "already completed."
            throw new IllegalStateException("Task '" + taskName + "' is already " + status.toLowerCase() + ".");
        }
        this.status = ASSIGNED;
    }

    /**
     * Marks the task as completed. Only allowed after it has been assigned.
     *
     * @throws IllegalStateException if the task has not been assigned yet
     */
    public void completeTask() {
        if (!status.equals(ASSIGNED)) {
            throw new IllegalStateException("Task must be assigned before it can be completed.");
        }
        this.status = COMPLETED;
    }

    /** @return true if the status is COMPLETED. */
    public boolean isCompleted() {
        return status.equals(COMPLETED);
    }

    /** @return the task ID. */
    public String getTaskId() {
        return taskId;
    }

    /** @return the task name. */
    public String getTaskName() {
        return taskName;
    }

    /** @return the task description. */
    public String getDescription() {
        return description;
    }

    /** @return the due date text. */
    public String getDueDate() {
        return dueDate;
    }

    /** @return the responsible department. */
    public String getDepartment() {
        return department;
    }

    /** @return the current status text. */
    public String getStatus() {
        return status;
    }
}
