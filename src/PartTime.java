/**
 * A part-time employee. Inherits from Employee.
 *
 * Reuses from Employee: all shared fields, getters, setters, and the printBasicInfo() helper.
 * Adds: a weekly work-hours value, with its own range check (1 to 40 hours).
 */
public class PartTime extends Employee {

    /** Hours worked per week. Private, and validated to be between 1 and 40. */
    private int workHours;

    /**
     * Creates a part-time employee.
     *
     * @param employeeId the unique ID
     * @param name       the full name
     * @param email      the email address
     * @param position   the job title
     * @param department the department
     * @param workHours  hours per week, from 1 to 40
     * @throws IllegalArgumentException if any value is invalid
     */
    public PartTime(String employeeId, String name, String email, String position,
                    String department, int workHours) {
        super(employeeId, name, email, position, department);   // initialize shared fields first
        this.workHours = ValidationUtil.requireInRange(workHours, 1, 40, "Work hours");
    }

    /**
     * Overrides the abstract method from Employee.
     * @return "Part-Time"
     */
    @Override
    public String getEmployeeType() {
        return "Part-Time";
    }

    /**
     * Overrides the abstract displayInfo(): prints the shared lines, then the work hours.
     */
    @Override
    public void displayInfo() {
        printBasicInfo();
        System.out.println("Work hours per week: " + workHours);
    }
}
