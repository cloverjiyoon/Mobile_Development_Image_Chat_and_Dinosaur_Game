package edu.northeastern.group33webapi.FinalProject.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.northeastern.group33webapi.FinalProjectActivity;
import edu.northeastern.group33webapi.R;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, register;
    private EditText username, password, repassword, email;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.registerBanner);
        banner.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        username = (EditText) findViewById(R.id.registerUsername);
        password = (EditText) findViewById(R.id.registerPassword);
        repassword = (EditText) findViewById(R.id.registerRepassword);
        email = (EditText) findViewById(R.id.registerEmail);

        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        progressBar.setVisibility(View.INVISIBLE);


    }

    public boolean isValidPassword(String pwd, EditText password) {
        boolean isValid = true;
        if (pwd.length() > 15 || pwd.length() < 8) {
            password.setError("Password must be less than 20 and more than 8 characters in length.");
            isValid = false;
            password.requestFocus();
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!pwd.matches(upperCaseChars)) {
            password.setError("Password must have at least one uppercase character");
            isValid = false;
            password.requestFocus();
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!pwd.matches(lowerCaseChars)) {
            password.setError("Password must have at least one lowercase character");
            isValid = false;
            password.requestFocus();
        }
        String numbers = "(.*[0-9].*)";
        if (!pwd.matches(numbers)) {
            password.setError("Password must have at least one number");
            isValid = false;
            password.requestFocus();
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBanner:
                startActivity(new Intent(this, loginActivity.class));
                break;
            case R.id.register:
                System.out.println(R.id.register);
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String repwd = repassword.getText().toString().trim();
        String emailVal = email.getText().toString().trim();

        if (name.isEmpty()) {
            username.setError("username is required");
            username.requestFocus();
            return;
        }
        if (pwd.isEmpty()) {
            password.setError("password is required");
            password.requestFocus();
            return;
        }
        if (!this.isValidPassword(pwd, password)) {
            return;
        }

        if (repwd.isEmpty()) {
            repassword.setError("the retyping of password is required");
            repassword.requestFocus();
            return;
        }

        if (!pwd.equals(repwd)) {
            Toast.makeText(registerActivity.this, "Please retype your password again", Toast.LENGTH_SHORT);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }

        mAuth.fetchSignInMethodsForEmail(emailVal).addOnCompleteListener(registerActivity.this, new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (!task.getResult().getSignInMethods().isEmpty()) {
                    email.setError("This email has been registered");
                    email.requestFocus();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailVal, pwd).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            gameUser user = new gameUser(name, pwd, emailVal, 0);
                            FirebaseDatabase.getInstance().getReference().child("gameUsers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(registerActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(registerActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(registerActivity.this, gameStartActivity.class));
                                    } else {
                                        Toast.makeText(registerActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(registerActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }

        });

    }

}
