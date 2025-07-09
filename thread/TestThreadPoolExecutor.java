import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolExecutor {

    public static void main(String[] args) {
        // Create a fixed-size thread pool with 5 threads.
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Submit 10 tasks to the thread pool.
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by thread: " + Thread.currentThread().getName());
                try {
                    // Simulate some work.
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Task " + taskId + " interrupted.");
                }
            });
        }

        // Shutdown the executor when all tasks are completed.
        executor.shutdown();

        try {
            // Wait for all tasks to complete or timeout after a specified time.
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Some tasks did not finish in 5 seconds!");
                // Optionally, force shutdown if tasks are taking too long.
                // executor.shutdownNow();
            }
            System.out.println("All tasks finished.");
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for tasks to finish.");
            Thread.currentThread().interrupt();
        }

        // Cast ExecutorService to ThreadPoolExecutor to access more advanced features
        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
            System.out.println("Completed task count: " + threadPoolExecutor.getCompletedTaskCount());
        }

    }
}
