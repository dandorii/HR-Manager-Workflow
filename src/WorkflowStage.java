/**
 * Abstract parent for one step of the onboarding workflow.
 *
 * Shared by three child stages (DocumentStage, TrainingStage, RoleStage), which each
 * do different work inside executeStage().
 *
 * OOP concepts shown here:
 * - Inheritance parent: the stage subclasses reuse stageName, completed, and isCompleted().
 * - Abstraction: executeStage() is abstract, so every stage type must define its own work.
 * - Runtime polymorphism: OnboardingWorkflow holds WorkflowStage references, and calling
 *   executeStage() runs the version in the real child object.
 */
public abstract class WorkflowStage {

    protected String stageName;   // protected (as shown in the UML): subclasses can read it directly
    protected boolean completed;  // protected: subclasses set it through markCompleted()

    /**
     * Creates a stage that is not yet completed.
     *
     * @param stageName the readable name shown in the status list; cannot be blank
     * @throws IllegalArgumentException if the name is blank
     */
    public WorkflowStage(String stageName) {
        this.stageName = ValidationUtil.requireText(stageName, "Stage name");
        this.completed = false;   // new stages always start incomplete
    }

    /** @return the stage name. */
    public String getStageName() {
        return stageName;
    }

    /** @return true if this stage has been completed. */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Marks the stage as done. protected: only this class and subclasses can call it,
     * so outside code cannot skip a stage.
     */
    protected void markCompleted() {
        this.completed = true;
    }

    /**
     * Performs the stage's work. Abstract: each child class implements its own version.
     * Calling this on a WorkflowStage variable runs the child's version (runtime polymorphism).
     */
    public abstract void executeStage();
}
