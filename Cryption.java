import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cryption {

    public static int executeCryption(String taskData) throws IOException {
        Task task = Task.fromString(taskData);
        IO env = new IO();
        int key = Integer.parseInt(env.readFile(".env"));

        File inputFile = new File(task.getFilePath());
        File outFile;

        if(task.getAction() == Task.Action.ENCRYPT) {
            Files.createDirectory(Paths.get("encrypted"));
            outFile = new File("encrypted/" + task.getFilePath());
        } else {
            Files.createDirectory(Paths.get("decrypted"));
            outFile = new File( "decrypted/" + task.getFilePath());
        }

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

//            if (!tempFile.renameTo(new File("final.txt"))) {
//                throw new IOException("Could not rename temp file");
//            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("Exiting the encryption/decryption at: " + now.format(formatter));

            return 0;
        } catch (IOException | NumberFormatException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
