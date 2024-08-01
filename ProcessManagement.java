import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessManagement {
    private final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public boolean submitToQueue(Task task) {
        try {
            taskQueue.put(task);
            executorService.submit(this::executeTask);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task submission was interrupted: " + e.getMessage());
            return false;
        }
    }

    private void executeTask() {
        try {
            Task task = taskQueue.take();
            System.out.println("Executing task: " + task);
            int result = Cryption.executeCryption(task.toString());
            System.out.println("Task completed with result: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task execution was interrupted: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error during task execution: " + e.getMessage());
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
