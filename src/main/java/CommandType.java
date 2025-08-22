public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE, INVALID;
    
    public static CommandType fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return INVALID;
        }
        
        String command = input.toLowerCase().trim().split(" ")[0];
        
        switch (command) {
            case "todo":
                return TODO;
            case "deadline":
                return DEADLINE;
            case "event":
                return EVENT;
            case "list":
                return LIST;
            case "mark":
                return MARK;
            case "unmark":
                return UNMARK;
            case "delete":
                return DELETE;
            case "bye":
                return BYE;
            default:
                return INVALID;
        }
    }
}