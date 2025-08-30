package duke.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoTest {
    
    @Test
    public void constructor_validDescription_createsUndoneTodo() {
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
    }
    
    @Test
    public void markAsDone_unmarkedTodo_becomesMarked() {
        Todo todo = new Todo("read book");
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
        
        todo.markAsDone();
        assertTrue(todo.isDone());
        assertEquals("X", todo.getStatusIcon());
    }
    
    @Test
    public void markAsUndone_markedTodo_becomesUnmarked() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertTrue(todo.isDone());
        
        todo.markAsUndone();
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
    }
    
    @Test
    public void toString_unmarkedTodo_correctFormat() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }
    
    @Test
    public void toString_markedTodo_correctFormat() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }
    
    @Test
    public void toJson_unmarkedTodo_correctFormat() {
        Todo todo = new Todo("read book");
        assertEquals("{\"type\":\"T\",\"done\":false,\"description\":\"read book\"}", todo.toJson());
    }
    
    @Test
    public void toJson_markedTodo_correctFormat() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("{\"type\":\"T\",\"done\":true,\"description\":\"read book\"}", todo.toJson());
    }
    
    @Test
    public void toJson_todoWithSpecialCharacters_escapesCorrectly() {
        Todo todo = new Todo("read \"book\" with \\backslash");
        String expected = "{\"type\":\"T\",\"done\":false,\"description\":\"read \\\"book\\\" with \\\\backslash\"}";
        assertEquals(expected, todo.toJson());
    }
}