package com.rushika.task;



import com.rushika.task.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Task documents. Spring Data handles implementation.
 * Provides basic CRUD methods automatically.
 */
public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Finds tasks whose name contains the given search string (case-insensitive).
     * This supports the required GET (find) tasks by name endpoint.
     */
    List<Task> findByNameContainingIgnoreCase(String name);
}

