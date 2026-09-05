package et;

import et.parser.Parser;
import et.storage.Storage;
import et.task.Task;
import et.ui.Ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the application boundary used by ET's graphical interface.
 */
class ETTest {
    @Test
    void getResponse_addThenList_returnsResponsesAndPersistsTask() {
        RecordingStorage storage = new RecordingStorage();
        ET et = new ET(new Ui(message -> { }), storage, new Parser());

        String addResponse = et.getResponse("todo read notes");
        String listResponse = et.getResponse("list");

        assertTrue(addResponse.contains("I've added this task"));
        assertTrue(addResponse.contains("[T][ ] read notes"));
        assertTrue(listResponse.contains("1.[T][ ] read notes"));
        assertEquals(1, storage.savedTasks.size());
        assertEquals("read notes", storage.savedTasks.get(0).getDescription());
    }

    @Test
    void getResponse_invalidCommand_returnsParserError() {
        ET et = new ET(new Ui(message -> { }), new RecordingStorage(), new Parser());

        String response = et.getResponse("remind me");

        assertEquals("I don't recognize that command. Try todo, deadline, event, list, find, mark, unmark, "
                + "delete, or bye.", response);
    }

    @Test
    void getCommandResult_bye_returnsFarewellAndExitSignal() {
        ET et = new ET(new Ui(message -> { }), new RecordingStorage(), new Parser());

        ET.CommandResult result = et.getCommandResult("bye");

        assertEquals("Bye. Hope to see you again soon!", result.response());
        assertTrue(result.shouldExit());
    }

    /** Stores tasks in memory so tests do not modify the application's data file. */
    private static class RecordingStorage extends Storage {
        private List<Task> savedTasks = List.of();

        @Override
        public List<Task> load() {
            return new ArrayList<>();
        }

        @Override
        public void save(List<Task> tasks) throws IOException {
            savedTasks = List.copyOf(tasks);
        }
    }
}
