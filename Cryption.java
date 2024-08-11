import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cryption {

    public static int executeCryption(String taskData) throws IOException {
        Task task = Task.fromString(taskData);
        IO env = new IO();
        int key = Integer.parseInt(env.readFile(".env"));

        File inputFile = new File(task.getFilePath());
        File outFile;

        // Determine the output file path
        Path inputPath = Paths.get(task.getFilePath());
        String action = task.getAction() == Task.Action.ENCRYPT ? "encrypted" : "decrypted";
        Path outputPath = Paths.get(inputPath.getParent().getParent().toString(), action + "_" + inputPath.getParent().getFileName());
        Path relativeInputPath = inputPath.getParent().relativize(inputPath);
        outFile = outputPath.resolve(relativeInputPath).toFile();

        // Ensure the parent directory of the output file exists
        outFile.getParentFile().mkdirs();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outFile)) {

            int ch;
            while ((ch = fis.read()) != -1) {
                if (task.getAction() == Task.Action.ENCRYPT) {
                    ch = (ch + key) % 256;
                } else {
                    ch = (ch - key + 256) % 256;
                }
                fos.write(ch);
            }

            return 0;
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}