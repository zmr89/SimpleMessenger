package com.example.simplemessenger;

public class User {
    String id;
    String name;
    String lastName;
    String age;
    Boolean onlineStatus;

    public User(String id, String name, String lastName, String age, Boolean onlineStatus) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.onlineStatus = onlineStatus;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }
}
