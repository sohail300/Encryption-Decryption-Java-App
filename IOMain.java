import java.io.IOException;

public class IOMain {
    public static void main(String[] args) {
     final String path = "text.txt";
     final String content = "Hey, its Sohail!";
     IO io = new IO();

     try {
         io.writeFile(path, content);
         System.out.println(io.readFile(path));
     } catch (IOException e){
         e.printStackTrace();
     }
    }
}