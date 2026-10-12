/**
 * The main "manager" class. It stores all employees and onboarding workflows and
 * provides the operations the menu uses.
 *
 * Array of objects (deviation from the UML, which shows List<...>):
 * - employees and workflows are fixed-size arrays with 20 slots each.
 * - employeeCount and workflowCount track how many slots are filled. Slots are filled from
 *   index 0 upward, so the valid records are always in indexes 0 .. count-1.
 * - Every loop runs only up to the count, never the full array length, so empty slots are skipped.
 * - Capacity is checked before each insert to prevent ArrayIndexOutOfBoundsException.
 *
 * Relationship: "Manages" Employee objects and holds OnboardingWorkflow objects.
 */
public class HROnboardingSystem {

    /** Maximum number of employees the array can hold. */
    public static final int MAX_EMPLOYEES = 20;
    /** Maximum number of onboarding workflows the array can hold. */
    public static final int MAX_WORKFLOWS = 20;

    // final: the array references never change (the array size is fixed for the life of the object)
    private final Employee[] employees = new Employee[MAX_EMPLOYEES];
    private int employeeCount = 0;   // occupied slots: valid indexes are 0 .. employeeCount-1
    private final OnboardingWorkflow[] workflows = new OnboardingWorkflow[MAX_WORKFLOWS];
    private int workflowCount = 0;   // occupied slots for workflows

    /** @return how many employees are currently stored. */
    public int getEmployeeCount() {
        return employeeCount;
    }

    /** @return how many workflows are currently stored. */
    public int getWorkflowCount() {
        return workflowCount;
    }

    /**
     * Checks whether no free slot remains in the employee array.
     * @return true if employeeCount has reached MAX_EMPLOYEES
     */
    public boolean isEmployeeCapacityFull() {
        return employeeCount >= MAX_EMPLOYEES;
    }

    /**
     * Checks whether an employee ID is already used.
     * @param employeeId the ID to check
     * @return true if a matching employee exists
     */
    public boolean isEmployeeIdTaken(String employeeId) {
        return findEmployee(employeeId) != null;
    }

    /**
     * Adds an employee to the next free slot. Checks, in order: not null, capacity, unique ID.
     * Called with a parent-type reference (Employee), so it accepts FullTime, PartTime, or Intern.
     *
     * @param employee the employee to add
     * @throws IllegalArgumentException if the employee is null or the ID is a duplicate
     * @throws IllegalStateException    if the array is full
     */
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        // Capacity check BEFORE inserting, so we never write past the end of the array
        if (isEmployeeCapacityFull()) {
            throw new IllegalStateException("Employee capacity reached (" + MAX_EMPLOYEES + ").");
        }
        if (isEmployeeIdTaken(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists: " + employee.getEmployeeId());
        }
        employees[employeeCount] = employee;   // store at the next free index
        employeeCount++;                        // then move the "next free" marker forward
    }

    /**
     * Prints every stored employee. Handles the empty case with a message.
     * Each employee's displayInfo() runs its own version (runtime polymorphism).
     */
    public void viewEmployees() {
        if (employeeCount == 0) {
            System.out.println("No employees registered yet.");
            return;
        }
        for (int i = 0; i < employeeCount; i++) {
            employees[i].displayInfo();   // the correct child version runs for each object
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Searches for an employee by ID. Uses a linear search over the occupied slots only.
     * Comparison ignores upper/lower case and surrounding spaces.
     *
     * @param employeeId the ID to find
     * @return the matching employee, or null if none is found (null is the "not found" result)
     */
    public Employee findEmployee(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].getEmployeeId().equalsIgnoreCase(employeeId.trim())) {
                return employees[i];
            }
        }
        return null;
    }

    /**
     * Creates and starts an onboarding workflow for an existing employee.
     * Checks, in order: employee exists, no workflow exists yet, workflow capacity.
     *
     * @param employeeId the ID of the employee to onboard
     * @return the new, started workflow
     * @throws IllegalArgumentException if the employee does not exist
     * @throws IllegalStateException    if a workflow already exists or the array is full
     */
    public OnboardingWorkflow startOnboarding(String employeeId) {
        Employee employee = requireEmployee(employeeId);   // throws if not found
        if (findWorkflow(employeeId) != null) {
            throw new IllegalStateException("Onboarding already exists for employee " + employeeId + ".");
        }
        if (workflowCount >= MAX_WORKFLOWS) {
            throw new IllegalStateException("Workflow capacity reached (" + MAX_WORKFLOWS + ").");
        }
        OnboardingWorkflow workflow = new OnboardingWorkflow(employee);
        workflow.startWorkflow();
        workflows[workflowCount] = workflow;   // store in the next free slot
        workflowCount++;
        return workflow;
    }

    /**
     * Finds the onboarding workflow for an employee ID, using a linear search.
     *
     * @param employeeId the employee ID to look for
     * @return the matching workflow, or null if none exists
     */
    public OnboardingWorkflow findWorkflow(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        for (int i = 0; i < workflowCount; i++) {
            // Compare IDs through the workflow's employee reference
            if (workflows[i].getEmployee().getEmployeeId().equalsIgnoreCase(employeeId.trim())) {
                return workflows[i];
            }
        }
        return null;
    }

    /**
     * Runs the next onboarding stage for an employee.
     *
     * @param employeeId the employee whose workflow should advance
     * @return the updated workflow (so the caller can display it)
     * @throws IllegalArgumentException if no workflow exists for this employee
     * @throws IllegalStateException    if the workflow cannot advance (not started or complete)
     */
    public OnboardingWorkflow advanceOnboarding(String employeeId) {
        OnboardingWorkflow workflow = requireWorkflow(employeeId);
        workflow.nextStage();
        return workflow;
    }

    /**
     * Prints the onboarding progress for an employee. The UML name is viewProgress(); the
     * employee ID is passed in as a parameter so the method knows which workflow to show.
     *
     * @param employeeId the employee whose progress to print
     * @throws IllegalArgumentException if no workflow exists for this employee
     */
    public void viewProgress(String employeeId) {
        requireWorkflow(employeeId).displayStatus();
    }

    /**
     * Updates an employee's email and/or position. A blank input means "keep the current value."
     * All new values are validated BEFORE any field changes, so a bad value cannot leave
     * the record half-updated.
     *
     * @param employeeId  the employee to update
     * @param newEmail    the new email, or blank to keep the current one
     * @param newPosition the new position, or blank to keep the current one
     * @throws IllegalArgumentException if the employee does not exist or a new value is invalid
     */
    public void updateEmployee(String employeeId, String newEmail, String newPosition) {
        Employee employee = requireEmployee(employeeId);

        // Step 1: validate both values (no changes yet)
        // Ternary: if the input is blank, reuse the old value; otherwise validate the new one
        String emailToSave = newEmail.isBlank()
                ? employee.getEmail()
                : ValidationUtil.requireEmail(newEmail);
        String positionToSave = newPosition.isBlank()
                ? employee.getPosition()
                : ValidationUtil.requireText(newPosition, "Position");

        // Step 2: apply the changes only after both checks passed
        employee.setEmail(emailToSave);
        employee.setPosition(positionToSave);
    }

    /**
     * Private helper that finds an employee or throws an error. Used by methods that
     * cannot continue without an employee.
     *
     * @param employeeId the ID to find
     * @return the employee (never null)
     * @throws IllegalArgumentException if no employee matches
     */
    private Employee requireEmployee(String employeeId) {
        Employee employee = findEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with ID " + employeeId + ".");
        }
        return employee;
    }

    /**
     * Private helper that finds a workflow or throws an error with a helpful hint.
     *
     * @param employeeId the employee ID whose workflow is needed
     * @return the workflow (never null)
     * @throws IllegalArgumentException if no workflow exists
     */
    private OnboardingWorkflow requireWorkflow(String employeeId) {
        OnboardingWorkflow workflow = findWorkflow(employeeId);
        if (workflow == null) {
            throw new IllegalArgumentException("No onboarding workflow for employee " + employeeId
                    + ". Start onboarding first.");
        }
        return workflow;
    }
}
