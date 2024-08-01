import java.util.Objects;

public class Task {

    enum Action {
        ENCRYPT,
        DECRYPT
    }

    private String filePath;
    private Action action;

    public Task(Action act, String filePath) {
        this.action =act;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public Action getAction() {
        return action;
    }

//    @override
    public String toString(){
        return filePath+","+(action==Action.ENCRYPT? "ENCRYPT": "DECRYPT");
    }

    public static Task fromString(String taskData){
        String[] parts = taskData.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid task data format");
        }

        String filePath = parts[0];
        Action action = (Objects.equals(parts[1], "ENCRYPT")) ? Action.ENCRYPT: Action.DECRYPT;

        return new Task(action, filePath);
    }
}
