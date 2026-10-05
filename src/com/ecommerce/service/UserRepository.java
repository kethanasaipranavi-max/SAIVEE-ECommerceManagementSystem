package com.ecommerce.service;

import com.ecommerce.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final Map<Integer, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }

    public boolean save(User user) {

        if (user == null) {
            return false;
        }

        users.put(user.getUserId(), user);

        return true;
    }

    public User findById(int userId) {

        return users.get(userId);
    }

    public boolean exists(int userId) {

        return users.containsKey(userId);
    }

    public User removeById(int userId) {

        return users.remove(userId);
    }

    public List<User> findAll() {

        return new ArrayList<>(users.values());
    }

    public int count() {

        return users.size();
    }

    public void clear() {

        users.clear();
    }
}