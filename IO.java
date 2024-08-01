import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

public class IO {
    public String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }

    public void writeFile(String path, String content) throws IOException {
        Files.writeString(Paths.get(path), content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public  void appendToFile(String path, String content) throws IOException {
        Files.writeString(Paths.get(path), content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public boolean fileExists(String path) throws IOException {
        return Files.exists(Paths.get(path));
    }
}