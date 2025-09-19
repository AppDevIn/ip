package edith.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.io.IOException;

public class FixedDurationTaskTest {
    
    @Test
    public void todo_withDuration_showsDurationInToString() {
        Todo todo = new Todo("read report");
        todo.setDuration("2h");
        
        assertTrue(todo.toString().contains("(duration: 2h)"));
        assertTrue(todo.toString().contains("[T][ ] read report"));
    }
    
    @Test
    public void todo_withoutDuration_noErrOr() {
        Todo todo = new Todo("simple task");
        
        assertFalse(todo.toString().contains("duration"));
        assertEquals("[T][ ] simple task", todo.toString());
    }
    
    @Test
    public void todo_setDurationString_parsesProperly() {
        Todo todo = new Todo("task");
        todo.setDuration("1h 30m");
        
        assertEquals(90, todo.getDuration().toMinutes());
        assertTrue(todo.toString().contains("(duration: 1h 30m)"));
    }
    
    @Test
    public void todo_setDurationObject_storesProperly() {
        Todo todo = new Todo("task");
        Duration duration = Duration.ofMinutes(120);
        todo.setDuration(duration);
        
        assertEquals(120, todo.getDuration().toMinutes());
        assertTrue(todo.toString().contains("(duration: 2h)"));
    }
    
    @Test
    public void todo_toJson_includesDuration() {
        Todo todo = new Todo("test task");
        todo.setDuration("2h 30m");
        
        String json = todo.toJson();
        assertTrue(json.contains("\"duration\":\"150\""));
        assertTrue(json.contains("\"type\":\"T\""));
        assertTrue(json.contains("\"description\":\"test task\""));
    }
    
    @Test
    public void todo_toJson_withoutDuration_excludesDurationField() {
        Todo todo = new Todo("test task");
        
        String json = todo.toJson();
        assertFalse(json.contains("duration"));
        assertTrue(json.contains("\"type\":\"T\""));
        assertTrue(json.contains("\"description\":\"test task\""));
    }
    
    @Test
    public void todo_fromJson_withDuration_restoresProperly() throws IOException {
        String json = "{\"type\":\"T\",\"done\":false,\"description\":\"test task\",\"duration\":\"120\"}";
        
        Todo todo = Todo.convertFromJson(json);
        
        assertEquals("test task", todo.getDescription());
        assertEquals(120, todo.getDuration().toMinutes());
        assertFalse(todo.isDone());
    }
    
    @Test
    public void todo_fromJson_withoutDuration_worksNormally() throws IOException {
        String json = "{\"type\":\"T\",\"done\":false,\"description\":\"test task\"}";
        
        Todo todo = Todo.convertFromJson(json);
        
        assertEquals("test task", todo.getDescription());
        assertNull(todo.getDuration());
        assertFalse(todo.isDone());
    }
    
    @Test
    public void todo_fromJson_withDurationAndDone_restoresBoth() throws IOException {
        String json = "{\"type\":\"T\",\"done\":true,\"description\":\"completed task\",\"duration\":\"60\"}";
        
        Todo todo = Todo.convertFromJson(json);
        
        assertEquals("completed task", todo.getDescription());
        assertEquals(60, todo.getDuration().toMinutes());
        assertTrue(todo.isDone());
    }
    
    @Test
    public void todo_roundtripJsonSerialization_preservesAllData() throws IOException {
        Todo original = new Todo("sample task");
        original.setDuration("3h 45m");
        original.markAsDone();
        
        String json = original.toJson();
        Todo restored = Todo.convertFromJson(json);
        
        assertEquals(original.getDescription(), restored.getDescription());
        assertEquals(original.getDuration(), restored.getDuration());
        assertEquals(original.isDone(), restored.isDone());
    }
}