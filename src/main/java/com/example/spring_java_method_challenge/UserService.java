package com.example.spring_java_method_challenge;

import com.example.spring_java_method_challenge.Models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Serving as a "database" for tasks
*/
public class UserService {

    private List<User> users;

    public UserService(List<User> initialUsers) {
        this.users = initialUsers;
    }

    public UserService() {
        this.users = new ArrayList<User>();
    }

    public List<User> getAll() {
        return this.users;
    }

    public Optional<User> findUserBy(int userId) {
        return users.stream().filter(u -> u.id() == userId).findFirst();
    }

    public User add(User newUser) {
        boolean exists = users.stream().anyMatch(u -> u.id() == newUser.id());
        if (exists) {
            throw new IllegalArgumentException(
                "User with id " + newUser.id() + " already exists."
            );
        }
        this.users.add(newUser);
        return newUser;
    }
}
