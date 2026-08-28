package et.task;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests searching tasks in a task list.
 */
class TaskListTest {
    @Test
    void findTasks_keywordMatchesDescriptionsIgnoringCase() {
        Task readBook = new Todo("read book");
        Task returnBook = new Deadline("return Book", LocalDateTime.of(2019, 6, 6, 0, 0), false);
        Task buyGroceries = new Todo("buy groceries");
        TaskList tasks = new TaskList(List.of(readBook, returnBook, buyGroceries));

        assertEquals(List.of(readBook, returnBook), tasks.findTasks("BOOK"));
    }

    @Test
    void findTasks_keywordDoesNotMatch_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertEquals(List.of(), tasks.findTasks("notes"));
    }
}
