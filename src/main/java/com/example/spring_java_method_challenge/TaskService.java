package com.example.spring_java_method_challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Serving as a "database" for tasks
*/
public class TaskService {

    private List<Models.Task> tasks;

    public TaskService(List<Models.Task> initialTasks) {
        this.tasks = initialTasks;
    }

    public TaskService() {
        this.tasks = new ArrayList<Models.Task>();
    }

    public List<Models.Task> getAll() {
        return this.tasks;
    }

    public List<Models.Task> forUser(int userId) {
        return tasks
            .stream()
            .filter(t -> t.userId() == userId)
            .collect(Collectors.toList());
    }

    public Models.Task add(Models.Task newTask) {
        this.tasks.add(newTask);

        return newTask;
    }
}
