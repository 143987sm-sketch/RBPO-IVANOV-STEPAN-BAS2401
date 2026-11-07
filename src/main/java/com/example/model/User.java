package com.example.model;

public final class User {

    private Long id;
    private String username;
    private String displayName;

    private User() {
    }

    public static User newInstance(Long id, String username, String displayName) {
        User user = new User();
        user.id = id;
        user.username = username;
        user.displayName = displayName;
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
