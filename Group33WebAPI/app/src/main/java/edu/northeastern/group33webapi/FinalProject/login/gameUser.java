package edu.northeastern.group33webapi.FinalProject.login;

import edu.northeastern.group33webapi.R;

public class gameUser {
    public String username, password, email;
    public int score, image;

    int[] userImages = {R.drawable.avator, R.drawable.avartar1, R.drawable.avartar2, R.drawable.avartar3, R.drawable.avartar4, R.drawable.avartar5, R.drawable.avartar6, R.drawable.avartar7, R.drawable.avartar8, R.drawable.avartar9, R.drawable.avartar10, R.drawable.avartar11, R.drawable.avartar12, R.drawable.avartar13, R.drawable.avartar14, R.drawable.avartar15};



    public gameUser(String username, String password, String email, int score, int image) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.score = score;

        int randomNumber = 0 + (int) (Math.random() * ((15 - 0) + 1));

        if(image == 0) {
            this.image = userImages[randomNumber];
        }
        else{
            this.image = image;
        }
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

    public int getImage() {
        return image;
    }

}

