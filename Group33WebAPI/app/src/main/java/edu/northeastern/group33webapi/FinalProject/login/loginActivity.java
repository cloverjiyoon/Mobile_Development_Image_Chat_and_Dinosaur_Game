package edu.northeastern.group33webapi.FinalProject.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.northeastern.group33webapi.R;

public class loginActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("") || pass.equals("") || repass.equals(""))
                    Toast.makeText(loginActivity.this, "Please enter all the required fields", Toast.LENGTH_SHORT);
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false){
                            Boolean insert = DB.insertData(user, pass);
                            if(insert == true){
                                Toast.makeText(loginActivity.this, "Registered successfully", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(getApplicationContext(), gameStartActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(loginActivity.this, "Registration failed", Toast.LENGTH_SHORT);

                            }
                        }else{
                            Toast.makeText(loginActivity.this, "User already exists! Please log in directly.", Toast.LENGTH_SHORT);
                        }
                    }else{
                        Toast.makeText(loginActivity.this, "Passwords not matching", Toast.LENGTH_SHORT);

                    }
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(loginActivity.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass == true){
                        Toast.makeText(loginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), gameStartActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(loginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });


    }
}