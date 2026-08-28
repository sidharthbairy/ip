/**
 * Provides a common task reference for commands that update one existing task.
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
}
