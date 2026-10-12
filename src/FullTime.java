/**
 * A permanent, full-time employee. Inherits from Employee (one level of inheritance).
 *
 * Reuses from Employee: all fields, getters, setters, and the printBasicInfo() helper.
 * Adds: the benefits field, which only full-time staff have.
 */
public class FullTime extends Employee {

    /** Benefits package description (for example, "Health insurance, 13th-month pay"). Private: only this class uses it. */
    private String benefits;

    /**
     * Creates a full-time employee.
     *
     * @param employeeId the unique ID
     * @param name       the full name
     * @param email      the email address
     * @param position   the job title
     * @param department the department
     * @param benefits   the benefits description; cannot be blank
     * @throws IllegalArgumentException if any value is invalid
     */
    public FullTime(String employeeId, String name, String email, String position,
                    String department, String benefits) {
        // super(...) runs the Employee constructor first, so the shared fields are set and validated
        super(employeeId, name, email, position, department);
        this.benefits = ValidationUtil.requireText(benefits, "Benefits");
    }

    /**
     * Overrides the abstract method from Employee.
     * @return "Full-Time"
     */
    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }

    /**
     * Overrides the abstract displayInfo(). Prints the shared lines through printBasicInfo(),
     * then adds this class's own line.
     */
    @Override
    public void displayInfo() {
        printBasicInfo();   // reuse the shared lines from Employee
        System.out.println("Benefits: " + benefits);
    }
}
