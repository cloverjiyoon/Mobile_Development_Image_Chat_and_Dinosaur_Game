package edu.northeastern.group33webapi.FinalProject.login;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.group33webapi.R;

public class forgetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

        emailEditText = (EditText) findViewById(R.id.resetEmail);
        resetPasswordButton = (Button) findViewById(R.id.resetpwd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }
    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide a valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(forgetPasswordActivity.this, new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(forgetPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(forgetPasswordActivity.this, "Try again! Something wrong happened.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
