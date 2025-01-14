package com.example.recommendor.models;

public class UserModel {
    private String id; // Firestore document ID
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String age;
    private String password;

    // Required empty constructor
    public UserModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password=password;}

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    // Utility method for getting the full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
