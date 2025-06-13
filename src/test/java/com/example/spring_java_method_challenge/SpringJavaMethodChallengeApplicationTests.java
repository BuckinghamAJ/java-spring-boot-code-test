package com.example.spring_java_method_challenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SpringJavaMethodChallengeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskController taskController;

    @Test
    void greetingDefaultMessage() throws Exception {
        mockMvc
            .perform(get("/greeting"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello, World!"));
    }

    @Test
    void greetingWithName() throws Exception {
        mockMvc
            .perform(get("/greeting").param("name", "Adam"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello, Adam!"));
    }

    @Test
    void addUserWithAllFields() throws Exception {
        String requestBody =
            """
            {
              "id": 10,
              "name": "Alice"
            }
            """;
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void addUserMissingId() throws Exception {
        String requestBody =
            """
            {
              "name": "Bob"
            }
            """;
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void addUserMissingName() throws Exception {
        String requestBody =
            """
            {
              "id": 11
            }
            """;
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void addUserWithExistingId() throws Exception {
        // First, add a user with id 12
        String requestBody =
            """
            {
              "id": 12,
              "name": "Charlie"
            }
            """;
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(12))
            .andExpect(jsonPath("$.name").value("Charlie"));

        // Try to add another user with the same id
        String duplicateRequestBody =
            """
            {
              "id": 12,
              "name": "Chuck"
            }
            """;
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(duplicateRequestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorCode").value(409))
            .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    void createTaskWithAllFields() throws Exception {
        String requestBody =
            """
            {
              "userId": 2,
              "title": "Read a book",
              "status": "DONE"
            }
            """;
        mockMvc
            .perform(
                post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.title").value("Read a book"))
            .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void createTaskWithDefaultStatus() throws Exception {
        String requestBody =
            """
            {
              "userId": 2,
              "title": "Write tests"
            }
            """;
        mockMvc
            .perform(
                post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.title").value("Write tests"))
            .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void createTaskWithoutUserId() throws Exception {
        String requestBody =
            """
            {
              "title": "Missing user"
            }
            """;
        mockMvc
            .perform(
                post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.errorMessage").value("No User ID provided"));
    }

    @Test
    void createTaskWithoutTitle() throws Exception {
        String requestBody =
            """
            {
              "userId": 3
            }
            """;
        mockMvc
            .perform(
                post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(
                jsonPath("$.errorMessage").value("No Task title provided")
            );
    }
}
