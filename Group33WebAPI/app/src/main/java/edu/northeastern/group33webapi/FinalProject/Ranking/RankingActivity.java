package edu.northeastern.group33webapi.FinalProject.Ranking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.northeastern.group33webapi.FinalProject.login.gameUser;
import edu.northeastern.group33webapi.R;

public class RankingActivity extends AppCompatActivity {

    private DatabaseReference myDataBase;
    private String userId;
    private HashMap users;
    private ArrayList<gameUser> userArrayList = new ArrayList<>();
//    int [] userImages = {R.drawable.Avatar};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myDataBase = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        setContentView(R.layout.activity_ranking);


        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        setUpUserModels();


        myDataBase.child("gameUsers").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    Log.e("firebase", "Empty data");
                    return;
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Get a Set of the keys in the map.
                        HashMap user = (HashMap) snapshot.getValue();
                        userArrayList.add(new gameUser(user.get("username").toString(), user.get("password").toString(), user.get("email").toString(), ((Long) Objects.requireNonNull(user.get("score"))).intValue()));

                    }
                    // Sort the list by last name using a custom Comparator.
                    Collections.sort(userArrayList, new Comparator<gameUser>() {
                        @Override
                        public int compare(gameUser p1, gameUser p2) {
                            return p2.getScore() - p1.getScore();
                        }
                    });

                    recyclerView.setAdapter(new user_RecyclerViewAdapter(this, userArrayList));
                }
            }
        });


    }

//    private void setUpUserModels(){
//        myDataBase = FirebaseDatabase.getInstance().getReference();
//        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//
//        myDataBase.child("gameUsers").get().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e("firebase", "Error getting data", task.getException());
//            } else {
//                HashMap tempMap = (HashMap) task.getResult().getValue();
//                if (tempMap == null) {
//                    Log.e("firebase", "Empty data");
//                    return;
//                }
//                this.users = tempMap;
//                // Get a Set of the keys in the map.
//                Set<String> keys = users.keySet();
//
//                // Iterate over the keys and print the key-value pairs.
//                for (String key : keys) {
//                    HashMap eachUser = (HashMap) users.get(key);
////                    gameUser userObject = new gameUser(eachUser.get("username").toString(), eachUser.get("password").toString(), eachUser.get("email").toString(), ((Long) Objects.requireNonNull(eachUser.get("score"))).intValue());
//
//                    userArrayList.add(new gameUser(eachUser.get("username").toString(), eachUser.get("password").toString(), eachUser.get("email").toString(), ((Long) Objects.requireNonNull(eachUser.get("score"))).intValue()));
//                }
//
//                // Sort the list by last name using a custom Comparator.
//                Collections.sort(userArrayList, new Comparator<gameUser>() {
//                    @Override
//                    public int compare(gameUser p1, gameUser p2) {
//                        return p2.getScore() - p1.getScore();
//                    }
//                });
//            }
//
//        });
//    }
}