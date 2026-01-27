package com.example.demogame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.demogame.models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private static UserManager instance;
    private Map<String, User> users;
    private User currentUser;
    private static final String PREFS_NAME = "RacingGamePrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_BALANCE = "balance";
    private static final double INITIAL_BALANCE = 10000.0;

    private UserManager() {
        users = new HashMap<>();
        initializeDefaultUsers();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        // Hardcoded users
        users.put("admin", new User("admin", "admin123", INITIAL_BALANCE));
        users.put("player1", new User("player1", "pass123", INITIAL_BALANCE));
        users.put("player2", new User("player2", "pass123", INITIAL_BALANCE));
        users.put("demo", new User("demo", "demo", INITIAL_BALANCE));
    }

    public boolean login(String username, String password, Context context) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            loadUserBalance(context);
            return true;
        }
        return false;
    }

    public boolean register(String username, String password, Context context) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        User newUser = new User(username, password, INITIAL_BALANCE);
        users.put(username, newUser);
        currentUser = newUser;
        saveUserBalance(context);
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void saveUserBalance(Context context) {
        if (currentUser != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_USERNAME, currentUser.getUsername());
            editor.putFloat(KEY_BALANCE, (float) currentUser.getBalance());
            editor.apply();
        }
    }

    public void loadUserBalance(Context context) {
        if (currentUser != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String savedUsername = prefs.getString(KEY_USERNAME, "");
            if (savedUsername.equals(currentUser.getUsername())) {
                float balance = prefs.getFloat(KEY_BALANCE, (float) INITIAL_BALANCE);
                currentUser.setBalance(balance);
            }
        }
    }
}
