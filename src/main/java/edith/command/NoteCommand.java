package edith.command;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;
import edith.exception.NoteException;

/**
 * Command for adding or updating a note on a task.
 * Takes a task number and note text, then attaches the note to the specified task.
 */
public class NoteCommand extends Command {
    private String input;
    
    /**
     * Creates a note command from the user's input.
     * 
     * @param input the full command string like "note 2 remember to bring laptop"
     */
    public NoteCommand(String input) {
        this.input = input;
    }
    
    /**
     * Adds or updates a note for the specified task and shows confirmation.
     * Converts from 1-based user numbering to 0-based array indexing.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String[] parts = input.split(" ", 3);
        
        if (parts.length < 3) {
            throw new NoteException("OOPS!!! Note format should be: note <task number> <note text>");
        }
        
        try {
            int taskNum = Integer.parseInt(parts[1]);
            String noteText = parts[2];
            
            if (noteText.trim().isEmpty()) {
                throw new NoteException("OOPS!!! Note cannot be empty.");
            }
            
            tasks.get(taskNum - 1).setNote(noteText);
            ui.showMessages(
                    " Got it! I've added a note to this task:",
                    "   " + tasks.get(taskNum - 1)
            );
            saveTasksToFile(tasks, ui, storage);
        } catch (NumberFormatException e) {
            throw new NoteException("OOPS!!! Task number must be a valid number.");
        } catch (IndexOutOfBoundsException e) {
            throw new NoteException("OOPS!!! Task number is out of range.");
        }
    }
}