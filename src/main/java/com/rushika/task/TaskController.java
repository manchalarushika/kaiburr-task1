package com.rushika.task;

//package com.rushika.task.controller;

import com.rushika.task.Task;
import com.rushika.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller defining all required endpoints for Task management, mapped to /tasks.
 * This layer handles HTTP requests and responses, delegating business logic to TaskService.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /tasks (Return all tasks)
     * GET /tasks?id={id} (Return single task by ID)
     * The ID check is handled by the service and returns 404 if not found.
     */
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            try {
                // If ID is provided, return a single task
                Task task = taskService.findTaskById(id);
                return ResponseEntity.ok(task);
            } catch (ResponseStatusException e) {
                // Catches the 404 thrown by findTaskById
                return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
            }
        } else {
            // If no ID is provided, return all tasks
            List<Task> allTasks = taskService.findAllTasks();
            return ResponseEntity.ok(allTasks);
        }
    }

    /**
     * GET /tasks/search?name={string}
     * Finds tasks by name containing the search string (case-insensitive).
     */
    @GetMapping("/search")
    public ResponseEntity<?> findTasksByName(@RequestParam String name) {
        try {
            List<Task> tasks = taskService.findTasksByName(name);
            return ResponseEntity.ok(tasks);
        } catch (ResponseStatusException e) {
            // Catches the 404 thrown by findTasksByName if the list is empty
            return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PUT /tasks
     * Creates or Updates a task object. The Service layer enforces command validation.
     */
    @PutMapping
    public ResponseEntity<?> saveTask(@RequestBody Task task) {
        try {
            Task savedTask = taskService.saveTask(task);
            // Returns 201 Created for a new resource/successful save operation
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Catches validation errors for malicious commands (400 Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE /tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    /**
     * PUT /tasks/execute/{id}
     * Executes the command associated with the task and records the execution history.
     */
    @PutMapping("/execute/{id}")
    public ResponseEntity<?> executeTask(@PathVariable String id) {
        try {
            Task updatedTask = taskService.executeTask(id);
            return ResponseEntity.ok(updatedTask);
        } catch (ResponseStatusException e) {
            // Catches 404 (not found) or 500 (execution error)
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}

