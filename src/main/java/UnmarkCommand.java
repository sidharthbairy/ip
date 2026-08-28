/**
 * Marks one task as not completed.
 */
public class UnmarkCommand extends TaskIndexCommand {
    /**
     * Creates a command that marks one task as not completed.
     *
     * @param taskIndex the zero-based index of the task to unmark
     */
    public UnmarkCommand(int taskIndex) {
        super(taskIndex);
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ETException {
        Task task = getTask(tasks);
        task.markAsNotDone();
        saveTasks(tasks, ui, storage);
        ui.showTaskUnmarked(task);
    }
}
