import java.io.IOException;

/**
 * Provides the common task reference and persistence behavior for commands
 * that update one existing task.
 */
public abstract class TaskIndexCommand extends Command {
    /** The zero-based index of the task affected by this command. */
    protected final int taskIndex;

    /**
     * Creates a command that affects one task.
     *
     * @param taskIndex the zero-based index of the affected task
     */
    protected TaskIndexCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Saves the current task list and reports an error if saving fails.
     *
     * @param tasks ET's current task list
     * @param ui the component used to communicate with the user
     * @param storage the component used to persist tasks
     */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSavingError();
        }
    }
}
