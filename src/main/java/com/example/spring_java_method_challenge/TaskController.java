package com.example.spring_java_method_challenge;

import com.example.spring_java_method_challenge.Models.Task;
import com.example.spring_java_method_challenge.Models.TaskStatus;
import com.example.spring_java_method_challenge.Models.User;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private static final String template = "Hello, %s!";

    private UserService userService = new UserService(
        new ArrayList<>(
            Arrays.asList(new User(1, "Bob", Models.Permission.USER))
        )
    );

    private TaskService taskService = new TaskService(
        new ArrayList<>(
            Arrays.asList(
                new Task(101, 1, "Buy groceries", TaskStatus.TODO),
                new Task(102, 2, "Walk the dog", TaskStatus.IN_PROGRESS)
            )
        )
    );

    /**
     * GET /greeting
     * Returns a greeting message.
     *
     * Query Parameters:
     *   - name (optional): The name to include in the greeting. Defaults to "World" if not provided.
     *
     * Response:
     *   - 200 OK: Returns a plain text greeting message, e.g., "Hello, Bob!"
     */
    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(value = "name", defaultValue = "World") String name
    ) {
        return String.format(template, name);
    }

    /**
     * GET /api/users/{id}
     * Retrieves a user by their unique ID.
     *
     * Path Parameters:
     *   - id (required): The ID of the user to retrieve.
     *
     * Responses:
     *   - 200 OK: Returns the user object if found.
     *   - 404 Not Found: If no user with the given ID exists.
     *   - 400 Bad Request: If the provided ID is not a valid integer.
     */
    @GetMapping("api/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            int userId = Integer.parseInt(id);
            Optional<User> user = this.userService.findUserBy(userId);

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found"
                    )
                );
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid user ID format"
                )
            );
        }
    }

    /**
     * POST /api/users
     * Creates a new user.
     *
     * Request Body:
     *   - User object containing:
     *       - id (required): The unique ID for the user.
     *       - name (required): The name of the user.
     *
     * Responses:
     *   - 201 OK: Returns the created user object.
     *   - 400 Bad Request: If required fields are missing or user with the same ID already exists.
     */
    @PostMapping("/api/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        if (user.id() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "No User ID provided"
                )
            );
        }

        if (user.name() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Name not provided"
                )
            );
        }

        try {
            User newUser = new User(user.id(), user.name());
            this.userService.add(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage())
            );
        }
    }

    /**
     * BUG:
     * GET /api/tasks/
     * This route should filter by `userId` if provided as a query param,
     * but right now it just returns all the tasks.
     *
     * Query Parameters:
     *   - userId (optional): If provided, should filter and return only tasks belonging to the specified user.
     *
     * Response:
     *   - 200 OK: Returns a list of tasks (optionally filtered by userId).
     */
    @GetMapping("/api/tasks")
    public ResponseEntity<?> getTasks() {
        return ResponseEntity.ok(this.taskService.getAll());
    }

    /**
     * POST /api/tasks
     * Creates a new task.
     *
     * Request Body:
     *   - Task object containing:
     *       - userId (required): The ID of the user to whom the task belongs.
     *       - title (required): The title or description of the task.
     *       - status (optional): The status of the task (e.g., TODO, IN_PROGRESS, DONE). Defaults to TODO if not provided.
     *
     * Responses:
     *   - 200 OK: Returns the created task object.
     *   - 400 Not Found: If no user ID or title is provided in the request body.
     */
    @PostMapping("/api/tasks")
    public ResponseEntity<?> newTask(@RequestBody Task task) {
        TaskStatus status = (task.status() == null)
            ? TaskStatus.TODO
            : task.status();

        if (task.userId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "No User ID provided"
                )
            );
        }

        if (task.title() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "No Task title provided"
                )
            );
        }

        Task newTask = new Task(task.userId(), task.title(), status);

        return ResponseEntity.ok(this.taskService.add(newTask));
    }
}
