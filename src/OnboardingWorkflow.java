import java.time.LocalDate;

/**
 * Tracks the full onboarding process for one employee.
 *
 * Each workflow owns a fixed array of three WorkflowStage objects. Because the array's
 * declared type is WorkflowStage[], it can hold DocumentStage, TrainingStage, and
 * RoleStage objects together (polymorphism and an array of objects).
 *
 * Implementation assumption: the workflow creates its own stages in its constructor
 * (composition). The UML "Uses" line is not clear enough to confirm this.
 */
public class OnboardingWorkflow {

    // Overall status constants
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "In Progress";
    public static final String COMPLETED = "Completed";

    private String workflowId;        // "WF-" plus the employee ID
    private Employee employee;        // the employee being onboarded
    private String currentStage;      // name of the next incomplete stage
    private String status;            // PENDING, IN_PROGRESS, or COMPLETED
    private int progress;             // percent complete, from 0 to 100
    private WorkflowStage[] stages;   // array of objects holding the three stages

    /**
     * Creates a workflow for an employee. Status starts as PENDING and progress as 0.
     * The three stages are created here and stored in the array.
     *
     * @param employee the employee to onboard; cannot be null
     * @throws IllegalArgumentException if employee is null
     */
    public OnboardingWorkflow(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        this.employee = employee;
        this.workflowId = "WF-" + employee.getEmployeeId();
        this.status = PENDING;
        this.progress = 0;
        this.stages = createDefaultStages(employee);
        this.currentStage = stages[0].getStageName();   // first stage is the starting point
    }

    /**
     * Builds the three onboarding stages, each holding its own item (Document, Training, Task).
     * Private helper: only the constructor needs it.
     *
     * @param employee the employee whose ID and department are used to name the items
     * @return an array of three WorkflowStage objects
     */
    private WorkflowStage[] createDefaultStages(Employee employee) {
        String id = employee.getEmployeeId();

        // Create the items each stage will manage
        Document document = new Document(id + "-DOC", "Employee Documents");
        Training training = new Training(id + "-TRN", "Company Orientation", "HR Training Team");
        // Due date: 14 days from today
        Task task = new Task(id + "-TSK", "Account and Role Setup",
                "Create company accounts and assign role",
                LocalDate.now().plusDays(14).toString(), employee.getDepartment());

        // Polymorphism: each array element has a different real class,
        // but all of them are stored as WorkflowStage references
        return new WorkflowStage[] {
            new DocumentStage("Document Submission", document),
            new TrainingStage("Orientation and Training", training),
            new RoleStage("Account and Role Setup", task)
        };
    }

    /**
     * Marks the workflow as started. Only allowed from PENDING.
     *
     * @throws IllegalStateException if onboarding has already started
     */
    public void startWorkflow() {
        if (!status.equals(PENDING)) {
            throw new IllegalStateException("Onboarding for " + employee.getName() + " has already started.");
        }
        this.status = IN_PROGRESS;
        System.out.println("Onboarding started for " + employee.getName() + ".");
    }

    /**
     * Runs the next incomplete stage, then updates progress and the current stage name.
     * Stages must be run in order, one at a time.
     *
     * @throws IllegalStateException if the workflow is not started or is already complete
     */
    public void nextStage() {
        // Order checks: the workflow must be started and not finished
        if (status.equals(PENDING)) {
            throw new IllegalStateException("Start the onboarding workflow before advancing stages.");
        }
        if (status.equals(COMPLETED)) {
            throw new IllegalStateException("Onboarding for " + employee.getName() + " is already completed.");
        }

        WorkflowStage stage = findNextIncompleteStage();
        stage.executeStage();   // runtime polymorphism: the child class's version runs
        System.out.println("Completed stage: " + stage.getStageName());

        updateProgress();
        // Find what comes next, or mark the whole workflow complete if nothing is left
        WorkflowStage next = findNextIncompleteStage();
        if (next == null) {
            this.status = COMPLETED;
            this.currentStage = "None (all stages complete)";
        } else {
            this.currentStage = next.getStageName();
        }
    }

    /**
     * Recalculates progress as a whole percentage of completed stages.
     * Formula: (completed stages * 100) / total stages, using integer division.
     * Example: 1 of 3 done gives 33, and 2 of 3 gives 66.
     */
    public void updateProgress() {
        int done = 0;
        // Loop over every stage and count the completed ones
        for (WorkflowStage stage : stages) {
            if (stage.isCompleted()) {
                done++;
            }
        }
        this.progress = (done * 100) / stages.length;
    }

    /** @return the progress percentage, from 0 to 100. */
    public int getProgress() {
        return progress;
    }

    /** @return the workflow status: PENDING, IN_PROGRESS, or COMPLETED. */
    public String getStatus() {
        return status;
    }

    /** @return the workflow ID. */
    public String getWorkflowId() {
        return workflowId;
    }

    /** @return the employee this workflow belongs to. */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Prints the workflow summary and a checklist of stages ([X] done, [ ] not done).
     */
    public void displayStatus() {
        System.out.println("--- Onboarding Status ---");
        System.out.println("Workflow ID: " + workflowId);
        System.out.println("Employee: " + employee.getName() + " (" + employee.getEmployeeId() + ")");
        System.out.println("Status: " + status);
        System.out.println("Progress: " + progress + "%");
        System.out.println("Current stage: " + currentStage);
        // Loop through the array and print a checkbox for each stage
        for (WorkflowStage stage : stages) {
            System.out.println("  [" + (stage.isCompleted() ? "X" : " ") + "] " + stage.getStageName());
        }
    }

    /**
     * Finds the first stage that is not complete. Searches in array order.
     *
     * @return the first incomplete stage, or null if all stages are complete
     */
    private WorkflowStage findNextIncompleteStage() {
        for (WorkflowStage stage : stages) {
            if (!stage.isCompleted()) {
                return stage;
            }
        }
        return null;   // null is the "not found" signal
    }
}
