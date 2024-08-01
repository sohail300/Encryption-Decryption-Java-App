import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the directory path: ");
        String directory = scanner.nextLine();

        System.out.print("Enter the action (encrypt/decrypt): ");
        String action = scanner.nextLine();

        try {
            Path directoryPath = Paths.get(directory);
            if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
                ProcessManagement processManagement = new ProcessManagement();

                try (Stream<Path> paths = Files.walk(directoryPath)) {
                    paths.filter(Files::isRegularFile)
                            .forEach(path -> {
                                String filePath = path.toString();
                                Task.Action taskAction = action.equals("encrypt") ? Task.Action.ENCRYPT : Task.Action.DECRYPT;
                                Task task = new Task(taskAction, filePath);

                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                System.out.println("Starting the encryption/decryption at: " + now.format(formatter));

                                processManagement.submitToQueue(task);
                            });
                }

                // Allow some time for tasks to complete
                Thread.sleep(5000);
                processManagement.shutdown();
            } else {
                System.out.println("Invalid directory path!");
            }
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            scanner.close();
        }
    }
}