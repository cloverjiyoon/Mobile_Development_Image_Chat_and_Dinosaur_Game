package edu.northeastern.group33webapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.provider.Settings.Secure;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.database.DatabaseReference;

import edu.northeastern.group33webapi.Firebase.SendNoteToFriendsActivity;
import edu.northeastern.group33webapi.Firebase.User;
import edu.northeastern.group33webapi.Firebase.Note;
import edu.northeastern.group33webapi.R;

public class FirebaseActivity extends AppCompatActivity {
    private DatabaseReference myDataBase;
    private EditText userNameInput;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        //set up all the UI view elements
        userNameInput = findViewById(R.id.editTextTextPersonName);
        registerBtn = findViewById(R.id.button_register);

        //set up the database reference
        myDataBase = FirebaseDatabase.getInstance().getReference();


        // Set on Click Listener on register button
        registerBtn.setOnClickListener(v -> registerUserAccount());
    }

    public void registerUserAccount() {
        //get the user input username data
        String userName;
        userName = userNameInput.getText().toString();
        //get the current device id
        @SuppressLint("HardwareIds") String android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        //validate for the user name input
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid user name",  Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MainActivity", "onCreate Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    DatabaseReference usersDatabase = myDataBase.child("users");

                    // https://stackoverflow.com/questions/49599308/how-to-check-key-in-firebase-database
                    usersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(userName).exists() && snapshot.child(userName).child(token).equals(token)) {
                                myDataBase.child("users").child(userName).get();
                            }else{
                                System.out.println("token from add for " + userName +  ":" + token);
                                User user = new User(userName, android_id, token);
                                myDataBase.child("users").child(userName).setValue(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                });

        Intent sendStickerToFriendsIntent = new Intent(this, SendNoteToFriendsActivity.class);
        sendStickerToFriendsIntent.putExtra("user_name", userName);
        startActivity(sendStickerToFriendsIntent);
    }
}