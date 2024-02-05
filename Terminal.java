import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.io.IOException;

public class Terminal {
    private Parser parser;
    private Path currentDirectory = Path.of(System.getProperty("user.dir"));


    public Terminal() {
        parser = new Parser();
    }

    public void handlingOutput(StringBuilder output){
        String[] args = parser.getArgs();
        boolean flag = parser.getRedirectionFlag();
        if (flag){ // The input contains > or >>
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (Objects.equals(args[i], ">") || Objects.equals(args[i], ">>")){
                    index = i;
                }
            }
            String filename = args[index + 1];
            Path filePath = currentDirectory.resolve(filename);
            if (Objects.equals(args[index], ">")){
                try{
                    PrintWriter outFile = new PrintWriter(new FileWriter(filePath.toFile()));
                    outFile.write(String.valueOf(output));
                    outFile.close();
                } catch (IOException e) {
                    System.out.println("An error occurred while writing on file: " + filename);
                }
            }
            else if(Objects.equals(args[index], ">>")){
                try{
                    PrintWriter outFile = new PrintWriter(new FileWriter(filePath.toFile(), true));
                    outFile.write(String.valueOf(output));
                    outFile.close();
                } catch (IOException e) {
                    System.out.println("An error occurred while writing on file: " + filename);
                }
            }
        }
        else{
            System.out.println(output);
        }
    }

    public void ls() {
        File dir = new File(currentDirectory.toString());
        File[] files = dir.listFiles();

        StringBuilder output = new StringBuilder();
        //list sorted alphabetically
        if(parser.argsLength()==0 || parser.argsLength() == 2) {
            ArrayList<File> SortedList = new ArrayList<>();
            for (File file : files) {
                SortedList.add(file);
            }
            Collections.sort(SortedList);

            for (File file : SortedList) {
                output.append(file.getName()).append("\n");
            }
            handlingOutput(output);
        }
        //reversed list
        else if(parser.argsLength()==1 || parser.argsLength() == 3){
            ArrayList<File> ReversedList = new ArrayList<>();

            for (File file : files) {

                ReversedList.add(file);
            }
            Collections.sort(ReversedList);
            Collections.reverse(ReversedList);

            for (File rfile : ReversedList) {
                output.append(rfile.getName()).append("\n");
            }
            handlingOutput(output);
        }

    }

    public void cat(){
        int FileRead;
        ArrayList<Character> FileContent = new ArrayList();
        String[] FileArgs = parser.getArgs();
        Path pathFile = currentDirectory.resolve(FileArgs[0]);

        try {
            // 1 file argument
            if (parser.argsLength() == 1 || parser.argsLength() == 3) {
                FileInputStream file1 = new FileInputStream(pathFile.toString());
                while((FileRead= file1.read())!=-1)
                {
                    FileContent.add((char)FileRead);
                }
                file1.close();
            }
         // 2 file arguments
            else if(parser.argsLength() == 2 || parser.argsLength() == 4){
                Path pathFile2 = currentDirectory.resolve(FileArgs[1]);

                FileInputStream file1= new FileInputStream(pathFile.toString());
                FileInputStream file2= new FileInputStream(pathFile2.toString());
                while((FileRead= file1.read())!=-1)
                {
                    FileContent.add((char)FileRead);
                }
                FileContent.add('\n');
                while((FileRead= file2.read())!=-1)
                {
                    FileContent.add((char)FileRead);
                }
                file1.close();
                file2.close();
            }
         //print the content of the file/s
            StringBuilder output = new StringBuilder();
            for(int i=0; i<FileContent.size(); i++){
                output.append(FileContent.get(i));
            }
            handlingOutput(output);
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }

    public String pwd() {
        // Implementation for the 'pwd' command
        // Return the current working directory
        return String.valueOf(currentDirectory);
    }

    public void history(){
        // Implementation for the 'history' command
        // Prints a list of all arguments used during runtime
        StringBuilder output = new StringBuilder();
        Vector<String> commandsUsed = parser.getCommands_used();
        for (int i = 0; i < parser.getCommands_used().size(); i++){
            output.append(i + 1);
            output.append(". " + commandsUsed.get(i) + "\n");
        }
        handlingOutput(output);
    }

    public void cd(String[] args) {
        if (args.length == 0){
            currentDirectory = Path.of(System.getProperty("user.home"));
        }
        else if (args.length == 1){
            if (Objects.equals(args[0], "..")){
                currentDirectory = currentDirectory.getParent();
            }
            else{
                currentDirectory = currentDirectory.resolve(args[0]);
            }
        }
    }

    public void rm(String fileName){
        // Implementation for the 'rm' command
        // Takes 1 file name that exists in the current dir as argument and removes it
        Path target = currentDirectory.resolve(fileName);
        if (!Files.exists(target)) {
            System.err.println("File does not exist: " + fileName);
            return;
        }
        if (Files.isRegularFile(target)) {
            try {
                Files.delete(target);
                System.out.println("Deleted file: " + fileName);
            } catch (IOException e) {
                System.err.println("An error occurred while deleting the file: " + e.getMessage());
            }
        } else {
            System.err.println("Not a regular file: " + fileName);
        }
    }

    public void mkdir(String[] args) {
        // Implementation for the 'mkdir' command
        // Create a new directory based on 'args'

        for (String arg : args) {
            Path newPath = currentDirectory.resolve(arg);

            try {
                // Create directories, including parent directories if they don't exist
                Files.createDirectories(newPath);
                System.out.println("Directory created: " + newPath.toString());
            } catch (IOException e) {
                System.out.println("Error creating directory " + newPath.toString() + ": " + e.getMessage());
            }
        }
    }

    public void touch(String[] args) {
        // Implementation for the 'touch' command
        // Create a file based on 'args'

        if (args.length != 1) {
            System.out.println("Invalid number of arguments for touch command.");
            return;
        }

        Path filePath = currentDirectory.resolve(args[0]);

        try {
            // Create the file
            Files.createFile(filePath);

        } catch (IOException e) {
            System.out.println("Error creating file " + filePath.toString() + ": " + e.getMessage());
        }
    }

    public void cp(String[] args) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid number of arguments for cp command.");
            return;
        }


        try {
            if (args[0].equals("-r")) {

                Path sourcePath = currentDirectory.resolve(args[1]);
                Path targetPath = currentDirectory.resolve(args[2]);

                copyDirectory(sourcePath, targetPath);
            } else {

                Path sourcePath = currentDirectory.resolve(args[0]);
                Path targetPath = currentDirectory.resolve(args[1]);

                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied from " + sourcePath.toString() + " to " + targetPath.toString());
            }
        } catch (IOException e) {
            System.out.println("Error copying: " + e.getMessage());
        }
    }

    public void echo(String[] args){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (Objects.equals(args[i], ">") || Objects.equals(args[i], ">>")){
                break;
            }
            output.append(args[i] + " ");
        }
        handlingOutput(output);
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        // Recursively copy the contents of the source directory to the target directory
        Files.walk(source)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = target.resolve(source.relativize(sourcePath));
                        if (Files.isDirectory(sourcePath)) {
                            Files.createDirectories(targetPath);
                        } else {
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        System.out.println("Error copying directory: " + e.getMessage());
                    }
                });
        System.out.println("Directory copied from " + source.toString() + " to " + target.toString());
    }

    public void rmdir(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Invalid number of arguments for rmdir command.");
            return;
        }

        Path newPath = currentDirectory.resolve(args[0]);

        if (args[0].equals("*")) {
            // remove all empty directories in the current directory

            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(currentDirectory)) {
                for (Path path : dirStream) {
                    if (Files.isDirectory(path) && isEmptyDirectory(path)) {
                        Files.delete(path);
                        System.out.println("Directory removed: " + path.toString());
                    }
                }
            } catch (IOException e) {
                System.out.println("Error removing directory " + newPath.toString() + ": " + e.getMessage());
            }
        } else {
            // Case 2: rmdir <path>
            // Remove the specified directory if it is empty
            if (isEmptyDirectory(newPath)) {
                Files.delete(newPath);
                System.out.println("Directory removed: " + newPath.toString());
            } else {
                System.out.println("Directory not empty or does not exist: " + newPath.toString());
            }
        }
    }

    private boolean isEmptyDirectory(Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }
    // Add other command methods here
    public void chooseCommandAction() {
        // Parse the user's input using the Parser
        boolean flag = true;

        while (flag) {
            System.out.print(currentDirectory.toString());
            System.out.print(">");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            if (parser.parse(input)) {
                String commandName = parser.getCommandName();
                String[] commandArgs = parser.getArgs();

                // Use a switch or if-else statements to determine which command to execute

                switch (commandName) {
                    case "ls":
                        ls();
                        System.out.print('\n');
                        break;
                    case "cat":
                        cat();
                        System.out.print('\n');
                        break;
                    case "echo":
                        echo(commandArgs);
                        System.out.println('\n');
                        break;
                    case "pwd":
                        String output = pwd();
                        handlingOutput(new StringBuilder(output));
                        System.out.print('\n');
                        break;
                    case "mkdir":
                        mkdir(commandArgs);
                        break;
                    case "rmdir":
                        try {
                            rmdir(commandArgs);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "history":
                        history();
                        break;
                    case "rm":
                        if (commandArgs.length == 1) {
                            rm(commandArgs[0]);
                            System.out.println();
                        } else {
                            System.err.println("Invalid 'rm' command. Usage: rm <filename>");
                        }
                        break;
                    case "touch":
                        touch(commandArgs);
                        break;
                    case "cp":
                        cp(commandArgs);
                        break;
                    case "cd": {
                        cd(commandArgs);
                        break;
                    }
                    case "exit":
                        flag=false;
                        break;

                    default:
                        System.out.println("Command not recognized: " + commandName);
                }
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.chooseCommandAction();
    }
}
