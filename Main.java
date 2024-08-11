import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select a directory to encrypt/decrypt");

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "No directory selected. Exiting.");
            return;
        }

        Path inputDirectory = fileChooser.getSelectedFile().toPath();

        String[] options = {"Encrypt", "Decrypt"};
        int actionChoice = JOptionPane.showOptionDialog(null, "Choose an action:", "Encrypt or Decrypt",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (actionChoice == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "No action selected. Exiting.");
            return;
        }

        String action = options[actionChoice].toLowerCase();
        Task.Action taskAction = action.equals("encrypt") ? Task.Action.ENCRYPT : Task.Action.DECRYPT;

        Path outputDirectory = inputDirectory.getParent().resolve(action + "ed_" + inputDirectory.getFileName());

        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error creating output directory: " + e.getMessage());
            return;
        }

        try {
            if (Files.exists(inputDirectory) && Files.isDirectory(inputDirectory)) {
                ProcessManagement processManagement = new ProcessManagement();

                try (Stream<Path> paths = Files.walk(inputDirectory)) {
                    paths.filter(Files::isRegularFile)
                            .forEach(path -> {
                                Path relativePath = inputDirectory.relativize(path);
                                Path outputPath = outputDirectory.resolve(relativePath);

                                try {
                                    Files.createDirectories(outputPath.getParent());
                                } catch (IOException e) {
                                    System.err.println("Error creating directories: " + e.getMessage());
                                    return;
                                }

                                // Create a Task with the input file path
                                Task task = new Task(taskAction, path.toString());

                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                System.out.println("Starting " + action + " for " + path + " at: " + now.format(formatter));

                                // Submit the task to the queue
                                processManagement.submitToQueue(task);
                            });
                }

                // Allow some time for tasks to complete
                Thread.sleep(5000);
                processManagement.shutdown();

                JOptionPane.showMessageDialog(null, "Operation completed successfully!\nOutput stored in: " + outputDirectory);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid directory path!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IO error: " + e.getMessage());
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}