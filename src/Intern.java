/**
 * A temporary intern. Inherits from Employee.
 *
 * Reuses from Employee: all shared fields, getters, setters, and the printBasicInfo() helper.
 * Adds: the school name and the internship duration in months (1 to 12).
 */
public class Intern extends Employee {

    /** Name of the school the intern attends. Private. */
    private String school;

    /** Internship length in months (1 to 12). Named "duration" to match the UML. */
    private int duration;

    /**
     * Creates an intern.
     *
     * @param employeeId the unique ID
     * @param name       the full name
     * @param email      the email address
     * @param position   the job title
     * @param department the department
     * @param school     the school name; cannot be blank
     * @param duration   internship length in months, from 1 to 12
     * @throws IllegalArgumentException if any value is invalid
     */
    public Intern(String employeeId, String name, String email, String position,
                  String department, String school, int duration) {
        super(employeeId, name, email, position, department);
        this.school = ValidationUtil.requireText(school, "School");
        this.duration = ValidationUtil.requireInRange(duration, 1, 12, "Internship duration");
    }

    /**
     * Overrides the abstract method from Employee.
     * @return "Intern"
     */
    @Override
    public String getEmployeeType() {
        return "Intern";
    }

    /**
     * Overrides the abstract displayInfo(): prints the shared lines, then the school and duration.
     */
    @Override
    public void displayInfo() {
        printBasicInfo();
        System.out.println("School: " + school);
        System.out.println("Duration: " + duration + " month(s)");
    }
}
