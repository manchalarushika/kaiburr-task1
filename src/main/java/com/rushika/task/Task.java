package com.rushika.task;

//package com.rushika.task.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main "Task" object stored in MongoDB.
 */
@Data
@Document(collection = "tasks")
public class
Task {
    /** Task ID - used as the MongoDB document primary key. */
    @Id
    private String id;

    /** Name of the task. */
    private String name;

    /** Owner of the task. */
    private String owner;

    /** The shell command string to be executed. */
    private String command;

    /** A list of historical execution records for this task. */
    private List<TaskExecution> taskExecutions = new ArrayList<>();

    // Ensures taskExecutions list is never null
    public List<TaskExecution> getTaskExecutions() {
        if (taskExecutions == null) {
            taskExecutions = new ArrayList<>();
        }
        return taskExecutions;
    }
}

