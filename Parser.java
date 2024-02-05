import java.util.Vector;

public class Parser {
    private String commandName;
    private String[] args;
    private boolean redirectionFlag = false;
    private Vector<String> commands_used= new Vector<>();

    public boolean parse(String input) {
        // Check if the input is not null or empty
        if (input == null || input.isEmpty()) {
            return false;
        }

        commands_used.add(input);
        redirectionFlag = (input.contains(">") || input.contains(">> "));

        // Split the input by whitespace into an array of tokens
        String[] tokens = input.split("\\s+");

        if (tokens.length == 0) {
            return false;
        }

        // The first token is the command name
        commandName = tokens[0];

        // The rest of the tokens are the arguments
        if (tokens.length > 1) {
            args = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
        } else {
            args = new String[0];
        }

        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public int argsLength(){
            return args.length;
     }

    public Vector<String> getCommands_used(){ return commands_used;}

    public boolean getRedirectionFlag(){ return redirectionFlag; }

//    public static void main(String[] args) {
//        Parser parser = new Parser();
//        String input = "command arg1 arg2 arg3";
//        if (parser.parse(input)) {
//            System.out.println("Command Name: " + parser.getCommandName());
//            System.out.println("Arguments: " + String.join(", ", parser.getArgs()));
//            commands_used.add(parser.getCommandName());
//        } else {
//            System.out.println("Invalid input.");
//        }
//    }
}
