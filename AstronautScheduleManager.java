///////////////////////////////// Astronaut Daily Schedule Organizer Programming Exercise //////////////////////////////////

////////////////////////////////////////////////////////// START ///////////////////////////////////////////////////////////

/////////////////////////////////////////////////// Importing Utilities ////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

////////////////////////////// Singleton Pattern: scheduleManager for managing astronaut tasks //////////////////////////////
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    // Add a new task with validation for time-overlap
    public boolean addTask(Task task) {
        for (Task t : tasks) {
            if (t.overlapsWith(task)) {
                System.out.println("Error: Task overlaps with existing task: " + t.getDescription());
                return false;
            }
        }
        tasks.add(task);
        System.out.println("Task added: " + task.getDescription());
        return true;
    }

    // Remove a task
    public void removeTask(String description) {
        tasks.removeIf(t -> t.getDescription().equals(description));
        System.out.println("Task removed: " + description);
    }

    // View all tasks, sorted by start-time
    public void viewTasks() {
        tasks.sort(Comparator.comparing(Task::getStartTime));
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            for (Task t : tasks) {
                System.out.println(t);
            }
        }
    }
}

//////////////////////// Task class with attributes: description, start-time, end-time, priority //////////////////////////
class Task {
    private String description;
    private String startTime;
    private String endTime;
    private String priority;

    public Task(String description, String startTime, String endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    // Check if this task overlaps with another task
    public boolean overlapsWith(Task other) {
        return (startTime.compareTo(other.endTime) < 0 && other.startTime.compareTo(endTime) < 0);
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime + "; " + description + " [" + priority + "]";
    }
}

//////////////////////////////// Command Pattern: command interface for task operations ///////////////////////////////////
interface TaskCommand {
    void execute();
}

////////////////////////////////////// AddTaskCommand: concrete command to add a task /////////////////////////////////////
class AddTaskCommand implements TaskCommand {
    private ScheduleManager manager;
    private Task task;

    public AddTaskCommand(ScheduleManager manager, Task task) {
        this.manager = manager;
        this.task = task;
    }

    @Override
    public void execute() {
        manager.addTask(task);
    }
}

///////////////////////////////// RemoveTaskCommand: concrete command to remove a task ////////////////////////////////////
class RemoveTaskCommand implements TaskCommand {
    private ScheduleManager manager;
    private String description;

    public RemoveTaskCommand(ScheduleManager manager, String description) {
        this.manager = manager;
        this.description = description;
    }

    @Override
    public void execute() {
        manager.removeTask(description);
    }
}

///////////////////////////////////// Main class for user-driven task managements /////////////////////////////////////////
public class AstronautScheduleManager {
    public static void main(String[] args) {
        ScheduleManager manager = ScheduleManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOptions: \n1. Add Task \n2. Remove Task \n3. View Tasks \n4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // handles the newline character

            if (choice == 1) {
                // gather details for adding a task
                System.out.print("Enter task description: ");
                String description = scanner.nextLine();

                System.out.print("Enter start time (HH:MM): ");
                String startTime = scanner.nextLine();

                System.out.print("Enter end time (HH:MM): ");
                String endTime = scanner.nextLine();

                System.out.print("Enter priority (High, Medium, Low): ");
                String priority = scanner.nextLine();

                Task task = new Task(description, startTime, endTime, priority);
                TaskCommand addTaskCommand = new AddTaskCommand(manager, task);
                addTaskCommand.execute();

            } else if (choice == 2) {
                // Get the task description to remove
                System.out.print("Enter the description of the task to remove: ");
                String description = scanner.nextLine();
                TaskCommand removeTaskCommand = new RemoveTaskCommand(manager, description);
                removeTaskCommand.execute();

            } else if (choice == 3) {
                // Display all scheduled tasks
                manager.viewTasks();

            } else if (choice == 4) {
                // Exit the program
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}

/////////////////////////////////////////////////////////// END ///////////////////////////////////////////////////////////