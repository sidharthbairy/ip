package et.gui;

import et.task.TaskType;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests extraction of task details from ET response lines.
 */
class TaskDisplayParserTest {
    @Test
    void parseTaskLine_numberedCompletedDeadline_returnsDisplayDetails() {
        Optional<TaskDisplayParser.TaskDisplay> result = TaskDisplayParser.parseTaskLine(
                "     2.[D][✓] return book (by: Dec 02 2019 6:00 PM)");

        assertTrue(result.isPresent());
        TaskDisplayParser.TaskDisplay display = result.get();
        assertEquals("2", display.taskNumber());
        assertEquals(TaskType.DEADLINE, display.taskType());
        assertTrue(display.isDone());
        assertEquals("return book (by: Dec 02 2019 6:00 PM)", display.description());
    }

    @Test
    void parseTaskLineUnnumberedIncompleteTodo_returnsDisplayDetails() {
        Optional<TaskDisplayParser.TaskDisplay> result = TaskDisplayParser.parseTaskLine("[T][ ] phone home");

        assertTrue(result.isPresent());
        assertEquals("", result.get().taskNumber());
        assertEquals(TaskType.TODO, result.get().taskType());
        assertFalse(result.get().isDone());
        assertEquals("phone home", result.get().description());
    }

    @Test
    void parseTaskLine_regularResponseText_returnsEmptyValue() {
        assertTrue(TaskDisplayParser.parseTaskLine("Here are the tasks in your list:").isEmpty());
    }
}
