package edu.northeastern.group33webapi.FinalProject.login;

public class gameUser {
    public String username, password, email;
    public int score;



    public gameUser(String username, String password, String email, int score){
            this.username = username;
            this.password = password;
            this.email = email;
            this.score = score;

        }


    public int getScore() {
        return this.score;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

}

