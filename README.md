# HR Onboarding Workflow

A console-based Java application that simulates how new employees are onboarded into a company. It stores employee records, starts an onboarding workflow for each employee, and tracks progress through three stages: document submission, orientation and training, and account/role setup.

## Project Objectives

- Apply core Object-Oriented Programming (OOP) concepts in a working program.
- Follow the structure of a UML class diagram.
- Practice input validation, exception handling, and fixed-size arrays of objects.
- Provide a menu-driven interface that is easy to test and present.

## Features

1. Add a new employee (Full-Time, Part-Time, or Intern)
2. View all employees
3. Search for an employee by ID
4. Start an onboarding workflow
5. Advance onboarding to the next stage
6. Display onboarding status and progress
7. Update an employee's email or position
8. Exit the program

## Classes and Responsibilities

| File | Type | Responsibility |
|---|---|---|
| `HROnboardingApp.java` | Concrete (entry point) | Shows the menu, reads keyboard input, and calls `HROnboardingSystem`. Contains `main`. |
| `HROnboardingSystem.java` | Concrete | Stores employees and workflows in fixed-size arrays. Provides add, view, search, start, advance, and update operations. |
| `Employee.java` | Abstract | Base class for all employees. Holds shared fields and getters/setters. |
| `FullTime.java` | Concrete | Full-time employee with a benefits description. |
| `PartTime.java` | Concrete | Part-time employee with weekly work hours (1 to 40). |
| `Intern.java` | Concrete | Intern with school name and internship duration (1 to 12 months). |
| `OnboardingWorkflow.java` | Concrete | Tracks one employee's onboarding: status, progress percentage, and three stages. |
| `WorkflowStage.java` | Abstract | Base class for one onboarding step. Declares `executeStage()`. |
| `DocumentStage.java` | Concrete | Stage that submits and verifies a `Document`. |
| `TrainingStage.java` | Concrete | Stage that schedules and completes a `Training`. |
| `RoleStage.java` | Concrete | Stage that assigns and completes a `Task`. |
| `Document.java` | Concrete | Document with the lifecycle Pending, Submitted, Verified. |
| `Training.java` | Concrete | Training session with the lifecycle Not Scheduled, Scheduled, Completed. |
| `Task.java` | Concrete | Setup task with the lifecycle Not Assigned, Assigned, Completed. |
| `ValidationUtil.java` | Utility (final) | Shared input checks: required text, numeric range, email format. |

## OOP Concepts Used

- **Encapsulation:** Fields are `private` or `protected`. Data changes go through validated constructors, getters, and setters (for example, `Employee.setEmail()`).
- **Inheritance:** `FullTime`, `PartTime`, and `Intern` extend `Employee`. `DocumentStage`, `TrainingStage`, and `RoleStage` extend `WorkflowStage`. Subclasses call `super(...)` in their constructors and `printBasicInfo()` (a protected helper in `Employee`) to reuse the shared printing code.
- **Polymorphism:**
  - Runtime: `Employee[]` holds `FullTime`, `PartTime`, and `Intern` objects. Calling `displayInfo()` or `getEmployeeType()` runs the correct child version. The same applies to `WorkflowStage[]` and `executeStage()`.
  - Compile-time: `Document` has two `submitDocument` methods with different parameters (overloading).
- **Exception handling:** `IllegalArgumentException` is used for invalid input. `IllegalStateException` is used for actions in the wrong order or a full array. `HROnboardingApp.run()` catches both and shows the message while keeping the menu running. `readInt()` re-prompts on bad numbers.
- **Arrays of objects:** `HROnboardingSystem` uses `Employee[20]` and `OnboardingWorkflow[20]`. `OnboardingWorkflow` uses `WorkflowStage[3]`.

## Notes on Fixed-Capacity Arrays and UML Deviations

- The UML diagram shows `List<Employee>` and `List<OnboardingWorkflow>`. The project requirements call for object arrays, so this implementation uses `Employee[20]` and `OnboardingWorkflow[20]`.
- `employeeCount` and `workflowCount` track occupied slots. Records fill indexes `0` to `count - 1`. Loops stop at the count, so empty slots are never read.
- Capacity is checked before every insert. Adding a 21st employee or 21st workflow shows a clear error instead of crashing.
- Search methods use a linear search and return `null` when nothing matches.
- The relationships between `WorkflowStage` subclasses and `Document`, `Training`, and `Task` are **implementation assumptions**. The UML lines for these were not clearly readable.
- `Employee.getEmployeeType()` and `Employee.displayInfo()` are both abstract, as the `<<abstract>>` marks in the UML suggest. The shared printing code lives in the protected helper `printBasicInfo()`, so it is not repeated in each child.
- **Naming conflict to confirm:** the UML shows `getEmployeeType()` in `Employee`, but `getType()` in `FullTime`, `PartTime`, and `Intern`. A method named `getType()` would not override the parent's `getEmployeeType()`, so the children implement `getEmployeeType()`. This is a deliberate choice, not a confirmed UML detail.
- `Intern` uses the UML attribute name `duration` (stored in months, 1 to 12).
- `Training` has the UML attribute `trainer`, which is set in the constructor. The UML shows `scheduleTraining()` with no parameters, so it sets the date to one week from today.
- `Document` keeps the UML method `submitDocument()` and adds an overload `submitDocument(String)`. The overload is an addition, used to demonstrate compile-time polymorphism.
- `HROnboardingSystem.viewProgress()` is implemented as `viewProgress(String employeeId)`. The UML shows no parameters, but the method needs to know which employee to display.
- `HROnboardingSystem.addEmployee()` takes an `Employee` parameter, and the search, workflow, and update methods were added. These are not in the UML.
- Constructors, `setEmail`, `setPosition`, `getPosition`, `getDepartment`, and `getTrainer` were added. They are not in the UML.
- The program uses no ArrayList, database, GUI library, or external dependency.

## Prerequisites

- **Java 21** (JDK 21 or newer). Java 17 or later will also work because the code uses `switch` arrows and `isBlank()`, but Java 21 is the tested target.
- A terminal, or VS Code with the Extension Pack for Java.

Check your version:

```
java -version
javac -version
```

## Compilation and Execution

From the project root (the folder that contains `src/`):

```
javac -d out src/*.java
java -cp out HROnboardingApp
```

- `javac -d out` puts the compiled `.class` files in an `out/` folder, which keeps them away from the source files.
- If your shell does not expand `*.java` (for example, Windows Command Prompt), list the files or use PowerShell.

### In VS Code

1. Choose **File > Open Folder** and select `HR_ONBOARDING_WORKFLOW`.
2. Open `src/HROnboardingApp.java`.
3. Click **Run** above `main`, or press **F5**.

## Sample Console Interaction

```
=== HR ONBOARDING WORKFLOW ===
========== MAIN MENU ==========
1. Add new employee
...
8. Exit
Select an option (1-8): 1
Employee type: 1) Full-Time  2) Part-Time  3) Intern
Select type (1-3): 1
Employee ID: E001
Full name: Maria Santos
Email: maria.santos@company.com
Position: Software Engineer
Department: IT
Benefits: Health insurance, 13th-month pay
Employee E001 added successfully as Full-Time.

Select an option (1-8): 4
Employee ID to onboard: E001
Onboarding started for Maria Santos.

Select an option (1-8): 5
Employee ID: E001
Completed stage: Document Submission
--- Onboarding Status ---
Progress: 33%
Current stage: Orientation and Training
  [X] Document Submission
  [ ] Orientation and Training
  [ ] Account and Role Setup
```

Invalid input is handled as well:

```
Select an option (1-8): 9
Please enter a number from 1 to 8.
Select an option (1-8): abc
Invalid input. Please enter a whole number.
```

## Project Structure

```
HR_ONBOARDING_WORKFLOW/
├── src/              All Java source files (15 files, default package)
├── README.md         This file
└── .gitignore        Excludes compiled output and IDE settings
```
