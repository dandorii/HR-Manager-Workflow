import java.util.Scanner;

/**
 * The console user interface and program entry point.
 *
 * Responsibility: show the menu, read keyboard input, and call HROnboardingSystem.
 * It does not store data or contain business rules. Those live in the domain classes.
 *
 * Exception handling: run() wraps each menu action in try-catch. The program catches
 * IllegalArgumentException and IllegalStateException, prints "Error: ...", and returns to
 * the menu instead of crashing.
 */
public class HROnboardingApp {

    /** Reads all keyboard input. Created once and closed when the program exits. */
    private final Scanner scanner = new Scanner(System.in);

    /** The business-logic object that holds all employee and workflow data. */
    private final HROnboardingSystem system = new HROnboardingSystem();

    /**
     * Program entry point. The JVM calls this method first.
     * It creates an app object (so instance fields exist) and starts the menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new HROnboardingApp().run();
    }

    /**
     * The main menu loop. Repeats until the user chooses Exit (option 8).
     */
    public void run() {
        System.out.println("=== HR ONBOARDING WORKFLOW ===");
        boolean running = true;   // controls the loop; set to false by option 8
        while (running) {
            printMenu();
            int choice = readInt("Select an option (1-8): ", 1, 8);
            try {
                // Arrow-style switch (Java 14+): each case runs one action
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> system.viewEmployees();
                    case 3 -> searchEmployee();
                    case 4 -> startOnboarding();
                    case 5 -> advanceOnboarding();
                    case 6 -> viewProgress();
                    case 7 -> updateEmployee();
                    case 8 -> running = false;   // ends the loop
                    default -> System.out.println("Unknown option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Multi-catch: both error types are shown the same way, and the menu continues
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Program closed. Goodbye!");
        scanner.close();   // release the keyboard input resource
    }

    /** Prints the list of menu options. */
    private void printMenu() {
        System.out.println("========== MAIN MENU ==========");
        System.out.println("1. Add new employee");
        System.out.println("2. View all employees");
        System.out.println("3. Search employee");
        System.out.println("4. Start onboarding workflow");
        System.out.println("5. Advance onboarding stage");
        System.out.println("6. Display onboarding status");
        System.out.println("7. Update employee information");
        System.out.println("8. Exit");
    }

    /**
     * Option 1: asks for employee details and adds the right subclass object.
     * The ID and capacity are checked before the rest of the questions are asked,
     * so the user does not type everything only to be rejected.
     */
    private void addEmployee() {
        if (system.isEmployeeCapacityFull()) {
            throw new IllegalStateException("Employee capacity reached (" + HROnboardingSystem.MAX_EMPLOYEES + ").");
        }
        System.out.println("Employee type: 1) Full-Time  2) Part-Time  3) Intern");
        int type = readInt("Select type (1-3): ", 1, 3);
        String id = ValidationUtil.requireText(readText("Employee ID: "), "Employee ID");
        if (system.isEmployeeIdTaken(id)) {
            throw new IllegalArgumentException("Employee ID already exists: " + id);
        }
        String name = readText("Full name: ");
        String email = readText("Email: ");
        String position = readText("Position: ");
        String department = readText("Department: ");

        // Switch expression: picks which subclass to create based on the menu choice.
        // The result is stored in an Employee variable (parent-class reference to a child object).
        Employee employee = switch (type) {
            case 1 -> new FullTime(id, name, email, position, department, readText("Benefits: "));
            case 2 -> new PartTime(id, name, email, position, department,
                    readInt("Work hours per week (1-40): ", 1, 40));
            default -> new Intern(id, name, email, position, department, readText("School: "),
                    readInt("Internship duration in months (1-12): ", 1, 12));
        };

        system.addEmployee(employee);
        System.out.println("Employee " + id + " added successfully as " + employee.getEmployeeType() + ".");
    }

    /**
     * Option 3: looks up an employee by ID and shows the details, or a not-found message.
     * A null result is handled here as a normal case, not an error.
     */
    private void searchEmployee() {
        String id = readText("Enter employee ID: ");
        Employee employee = system.findEmployee(id);
        if (employee == null) {
            System.out.println("No employee found with ID " + id + ".");
            return;
        }
        employee.displayInfo();   // polymorphic call
    }

    /** Option 4: reads an employee ID and starts that employee's onboarding. */
    private void startOnboarding() {
        system.startOnboarding(readText("Employee ID to onboard: "));
    }

    /** Option 5: advances one stage, then prints the updated status. */
    private void advanceOnboarding() {
        system.advanceOnboarding(readText("Employee ID: ")).displayStatus();
    }

    /** Option 6: prints the onboarding progress for an employee (UML method viewProgress). */
    private void viewProgress() {
        system.viewProgress(readText("Employee ID: "));
    }

    /**
     * Option 7: asks for new values. Pressing Enter (blank input) keeps the current value.
     */
    private void updateEmployee() {
        String id = readText("Employee ID to update: ");
        System.out.println("Leave a field blank to keep its current value.");
        String email = readText("New email: ");
        String position = readText("New position: ");
        system.updateEmployee(id, email, position);
        System.out.println("Employee " + id + " updated.");
    }

    /**
     * Reads a whole number and repeats the prompt until the input is valid.
     * This is the main protection against bad numeric input.
     *
     * @param prompt the question to show
     * @param min    the smallest allowed value
     * @param max    the largest allowed value
     * @return a valid number inside [min, max]
     */
    private int readInt(String prompt, int min, int max) {
        while (true) {   // loops until a valid value is returned
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                // parseInt throws NumberFormatException for text such as "abc"
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads one line of text and removes surrounding spaces.
     *
     * @param prompt the question to show
     * @return the trimmed text (may be empty)
     */
    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
