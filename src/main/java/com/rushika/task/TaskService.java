package com.rushika.task;

//package com.rushika.task.TaskService;

import com.rushika.task.Task;
import com.rushika.task.TaskExecution;
import com.rushika.task.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Core business logic for Task management and execution.
 * Handles CRUD operations, search, and the actual shell command execution.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CommandValidator commandValidator;
    private static final long TIMEOUT_SECONDS = 5; // Shell command timeout

    public TaskService(TaskRepository taskRepository, CommandValidator commandValidator) {
        this.taskRepository = taskRepository;
        this.commandValidator = commandValidator;
    }

    // --- CRUD and Search Operations ---

    /**
     * Returns all Task objects from the database.
     */
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Finds a single Task by its ID. Throws 404 if not found.
     */
    public Task findTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id));
    }

    /**
     * Finds tasks whose name contains the given search string (case-insensitive).
     * Throws 404 if no tasks are found.
     */
    public List<Task> findTasksByName(String name) {
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name);
        if (tasks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found matching name: " + name);
        }
        return tasks;
    }

    /**
     * Creates or updates a Task object. Enforces command validation before saving.
     */
    public Task saveTask(Task task) {
        // REQUIRED: Validate command before saving or updating
        commandValidator.validateCommand(task.getCommand());
        return taskRepository.save(task);
    }

    /**
     * Deletes a Task by its ID. Throws 404 if the task doesn't exist.
     */
    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // --- Execution Logic (PUT TaskExecution) ---

    /**
     * Executes the task's shell command locally and records the execution history.
     * Throws 500 if execution fails or times out.
     */
    public Task executeTask(String taskId) {
        Task task = findTaskById(taskId);
        String command = task.getCommand();

        // Security check is redundant here if it's already validated on PUT /tasks,
        // but it provides an extra layer of defense against corrupted data.
        commandValidator.validateCommand(command);

        Instant startTime = Instant.now();
        String output;

        try {
            // Determine OS and format command for shell execution (Windows vs. Unix)
            String osName = System.getProperty("os.name").toLowerCase();
            String[] commandArray = osName.contains("win")
                    ? new String[]{"cmd", "/c", command}
                    : new String[]{"sh", "-c", command};

            Process process = Runtime.getRuntime().exec(commandArray);

            // Wait for completion or timeout
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new RuntimeException("Command execution timed out after " + TIMEOUT_SECONDS + " seconds.");
            }

            // Capture Output and Error Streams
            String stdout = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
            String stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines().collect(Collectors.joining("\n"));

            // Combine output, prioritizing any error output
            output = (stdout.isEmpty() ? "" : stdout) + (stderr.isEmpty() ? "" : "\n[STDERR]\n" + stderr);
            output = output.trim();

            if (output.isEmpty()) {
                output = "[INFO] Command completed successfully with no output.";
            }

        } catch (Exception e) {
            System.err.println("Error executing command: " + command + " - " + e.getMessage());
            output = "[ERROR] Execution failed: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Command execution failed: " + e.getMessage(), e);
        }

        // Create and store the execution record
        TaskExecution newExecution = TaskExecution.builder()
                .startTime(startTime)
                .endTime(Instant.now())
                .output(output)
                .build();

        task.getTaskExecutions().add(newExecution);
        return taskRepository.save(task);
    }
}

