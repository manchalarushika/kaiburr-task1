package com.rushika.task;

//package com.rushika.task.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Represents a single execution record for a shell command, embedded within the Task document.
 */
@Data
@Builder
public class TaskExecution {
    /**
     * The date/time when the shell command execution started.
     */
    private Instant startTime;

    /**
     * The date/time when the shell command execution finished.
     */
    private Instant endTime;

    /**
     * The captured output (stdout and stderr) from the executed command.
     */
    private String output;
}
