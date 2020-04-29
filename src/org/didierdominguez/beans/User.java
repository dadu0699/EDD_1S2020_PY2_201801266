package org.didierdominguez.beans;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String career;
    private String password;
    private User nextUser;

    public User(int id, String name, String lastName, String career, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.career = career;
        this.password = password;
        nextUser = null;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getNextUser() {
        return nextUser;
    }

    public void setNextUser(User nextUser) {
        this.nextUser = nextUser;
    }
}
