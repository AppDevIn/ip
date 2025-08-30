package duke.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import duke.command.Command;
import duke.command.FindCommand;
import duke.exception.FindException;

public class FindParserTest {
    
    @Test
    public void parse_validFindCommand_returnsFindCommand() throws Exception {
        Command command = Parser.parse("find book", 5);
        assertTrue(command instanceof FindCommand);
    }
    
    @Test
    public void parse_emptyFindCommand_throwsFindException() {
        assertThrows(FindException.class, () -> {
            Parser.parse("find", 5);
        });
    }
    
    @Test
    public void parse_findWithOnlySpaces_throwsFindException() {
        assertThrows(FindException.class, () -> {
            Parser.parse("find   ", 5);
        });
    }
    
    @Test
    public void parse_validFindWithMultipleWords_returnsFindCommand() throws Exception {
        Command command = Parser.parse("find book club", 5);
        assertTrue(command instanceof FindCommand);
    }
}