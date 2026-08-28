package et.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and provides controlled access to ET's tasks.
 */
public class TaskList {
    /** The tasks currently managed by ET. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks the tasks to manage
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param taskIndex the zero-based task position
     * @return the task at the given position
     */
    public Task getTask(int taskIndex) {
        return tasks.get(taskIndex);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param taskIndex the zero-based task position
     * @return the task that was removed
     */
    public Task removeTask(int taskIndex) {
        return tasks.remove(taskIndex);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an immutable snapshot of the tasks for display or persistence.
     *
     * @return the current tasks in list order
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
}
