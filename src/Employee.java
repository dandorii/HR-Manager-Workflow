/**
 * Abstract base class for every employee type in the company.
 *
 * OOP concepts shown here:
 * - Encapsulation: fields are protected (not public), so only this class and its subclasses
 *   can reach them directly. Outside code must use the getters and setters.
 * - Abstraction: the class is abstract, so you cannot create a plain "Employee" object.
 *   Only FullTime, PartTime, or Intern objects can be created.
 * - Inheritance parent: FullTime, PartTime, and Intern extend this class and reuse its fields,
 *   getters, setters, and the printBasicInfo() helper.
 * - Polymorphism: getEmployeeType() and displayInfo() are both marked <<abstract>> in the UML,
 *   so each child class must provide its own version of both.
 * - Code reuse: printBasicInfo() holds the printing code shared by all children, so each child
 *   calls it instead of copying the same lines.
 */
public abstract class Employee {

    // protected (as shown in the UML): child classes can read these fields directly
    protected String employeeId;   // unique key used when searching (for example, "E001")
    protected String name;         // full name
    protected String email;        // must pass ValidationUtil.requireEmail
    protected String position;     // job title (for example, "Software Engineer")
    protected String department;   // department name (for example, "IT")

    /**
     * Creates an employee and validates every value.
     * Each child constructor calls this one first using super(...), so the shared fields
     * are initialized in one place and the validation runs only once.
     *
     * @param employeeId the unique ID; cannot be blank
     * @param name       the full name; cannot be blank
     * @param email      the email address; must look like name@company.com
     * @param position   the job title; cannot be blank
     * @param department the department; cannot be blank
     * @throws IllegalArgumentException if any value is invalid
     */
    public Employee(String employeeId, String name, String email, String position, String department) {
        // "this." makes it clear we are setting the field, not the parameter with the same name
        this.employeeId = ValidationUtil.requireText(employeeId, "Employee ID");
        this.name = ValidationUtil.requireText(name, "Name");
        this.email = ValidationUtil.requireEmail(email);
        this.position = ValidationUtil.requireText(position, "Position");
        this.department = ValidationUtil.requireText(department, "Department");
    }

    /** @return the unique employee ID (read-only: there is no setter, so the ID cannot change). */
    public String getEmployeeId() {
        return employeeId;
    }

    /** @return the employee's full name. */
    public String getName() {
        return name;
    }

    /** @return the employee's email address. */
    public String getEmail() {
        return email;
    }

    /** @return the job title. */
    public String getPosition() {
        return position;
    }

    /** @return the department name. */
    public String getDepartment() {
        return department;
    }

    /**
     * Changes the email address. The setter validates the value first, so an invalid email
     * is rejected and the old value is kept.
     *
     * @param email the new email
     * @throws IllegalArgumentException if the email format is invalid
     */
    public void setEmail(String email) {
        this.email = ValidationUtil.requireEmail(email);
    }

    /**
     * Changes the job title after validation.
     *
     * @param position the new job title; cannot be blank
     * @throws IllegalArgumentException if the value is blank
     */
    public void setPosition(String position) {
        this.position = ValidationUtil.requireText(position, "Position");
    }

    /**
     * Returns the type label of this employee (for example, "Full-Time").
     * Abstract: every child class must supply its own version. Calling this method on an
     * Employee variable runs the version in the actual child object (runtime polymorphism).
     *
     * @return the employee type as readable text
     */
    public abstract String getEmployeeType();

    /**
     * Prints the information for this employee. Abstract: each child prints the shared lines
     * (by calling printBasicInfo()) and then its own extra fields.
     */
    public abstract void displayInfo();

    /**
     * Prints the fields shared by all employee types. Children call this from their
     * displayInfo() methods so the shared printing code is written only once.
     * protected: available to subclasses, but not to outside code.
     */
    protected void printBasicInfo() {
        System.out.println("ID: " + employeeId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Position: " + position);
        System.out.println("Department: " + department);
        // Polymorphic call: prints the type of the real object (FullTime, PartTime, or Intern)
        System.out.println("Type: " + getEmployeeType());
    }
}
