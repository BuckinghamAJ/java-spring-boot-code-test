package com.example.spring_java_method_challenge;

import java.text.SimpleDateFormat;
import java.util.Date;

/*


*/

public class Models {

    public enum Permission {
        ADMIN,
        USER,
    }

    public enum TaskStatus {
        TODO("To Do"),
        IN_PROGRESS("In Progress"),
        DONE("Done");

        private final String label;

        TaskStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public record Task(int id, int userId, String title, TaskStatus status) {
        public Task(int userId, String title) {
            this(generateId(), userId, title, TaskStatus.TODO);
        }

        public Task(int userId, String title, TaskStatus status) {
            this(generateId(), userId, title, status);
        }

        // Helper method to generate ID
        private static int generateId() {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
            return Integer.parseInt(formatter.format(date));
        }
    }

    public record User(int id, String name, Permission permission) {
        public User(int id, String name) {
            this(id, name, Permission.USER);
        }
    }
}
