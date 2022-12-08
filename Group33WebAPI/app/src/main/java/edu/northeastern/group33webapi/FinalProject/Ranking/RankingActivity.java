package edu.northeastern.group33webapi.FinalProject.Ranking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.group33webapi.R;

public class RankingActivity extends AppCompatActivity {

    private DatabaseReference myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDataBase = FirebaseDatabase.getInstance().getReference();
//        userId = Settings.Secure.getString(
//                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        myDataBase.child("gameUsers").child(userId).get().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e("firebase", "Error getting data", task.getException());
//            } else {
//                HashMap tempMap = (HashMap) task.getResult().getValue();
//                if (tempMap == null) {
//                    Log.e("firebase", "Empty data");
//                    return;
//                }
//                username = Objects.requireNonNull(tempMap.get("username")).toString();
//            }
//        });


        setContentView(R.layout.activity_ranking);


    }
}