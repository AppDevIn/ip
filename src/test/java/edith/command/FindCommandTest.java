package edith.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Todo;
import edith.task.Deadline;
import edith.task.Event;
import edith.task.DateTimeParser;
import edith.ui.Ui;
import edith.exception.EdithException;

public class FindCommandTest {

    @Test
    public void execute_findWithMatchingTasks_displaysCorrectResults() throws EdithException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Deadline("return book", DateTimeParser.parseDateTime("6/6/2023 1800")));
        tasks.add(new Event("book club meeting",
                DateTimeParser.parseDateTime("10/6/2023 1900"),
                DateTimeParser.parseDateTime("10/6/2023 2100")));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        FindCommand findCommand = new FindCommand("find book");
        Ui ui = new Ui();
        Storage storage = new Storage("data", "test.txt");

        findCommand.execute(tasks, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Scan results - matching tasks located:"));
        assertTrue(output.contains("1.[T][ ] read book"));
        assertTrue(output.contains("3.[D][ ] return book"));
        assertTrue(output.contains("4.[E][ ] book club meeting"));
    }

    @Test
    public void execute_findWithNoMatches_displaysNoResults() throws EdithException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Todo("do homework"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        FindCommand findCommand = new FindCommand("find book");
        Ui ui = new Ui();
        Storage storage = new Storage("data", "test.txt");

        findCommand.execute(tasks, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Scan complete. No matching tasks found."));
    }

    @Test
    public void execute_caseInsensitiveSearch_findsMatches() throws EdithException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("Read Book"));
        tasks.add(new Todo("BOOK REVIEW"));
        tasks.add(new Todo("buy groceries"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        FindCommand findCommand = new FindCommand("find book");
        Ui ui = new Ui();
        Storage storage = new Storage("data", "test.txt");

        findCommand.execute(tasks, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Scan results - matching tasks located:"));
        assertTrue(output.contains("1.[T][ ] Read Book"));
        assertTrue(output.contains("2.[T][ ] BOOK REVIEW"));
    }
}
