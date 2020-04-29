package org.didierdominguez.beans;

public class SessionProperties {
    private static SessionProperties instance;
    private User user;

    private SessionProperties() {
        this.user = null;
    }

    public static SessionProperties getInstance() {
        if (instance == null) {
            instance = new SessionProperties();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
