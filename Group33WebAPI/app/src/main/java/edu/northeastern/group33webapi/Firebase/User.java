package edu.northeastern.group33webapi.Firebase;

public class User {
    public String username;
    public String userDeviceID;
    public String token;

    public User(String username, String userDeviceID, String token) {
        this.username = username;
        this.userDeviceID = userDeviceID;
        this.token = token;
    }
}
