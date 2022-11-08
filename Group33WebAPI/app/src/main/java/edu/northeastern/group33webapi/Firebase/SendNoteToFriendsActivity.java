package edu.northeastern.group33webapi.Firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.northeastern.group33webapi.R;

public class SendNoteToFriendsActivity extends AppCompatActivity {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private static String SERVER_KEY;

    private String myName;
    private TextView myNameTV;
    private DatabaseReference myDataBase;
    private Spinner allFriends;
    private int imageNum = 6;
    private Map<ImageView, Integer> imageViewSentCountMap = new HashMap<>();
    private Map<ImageView, Boolean> imageViewIsClickedMap = new HashMap<>();
    private Map<ImageView, TextView> imageViewToTextViewMap = new HashMap<>();
    private Map<Integer, Integer> imageViewIdToId = new HashMap<>();
    private Map<String, String> userNameToUserIdMap = new HashMap<>();
    private Map<String, String> userIdToUserNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myName = extras.getString("user_name");
        }
        SERVER_KEY = "key=" + getProperties(getApplicationContext()).getProperty("SERVER_KEY");
        setContentView(R.layout.activity_send_note_to_friends);
        createNotificationChannel();
        allFriends = findViewById(R.id.friend_spinner);
        myDataBase = FirebaseDatabase.getInstance().getReference();

        myNameTV = (TextView) findViewById(R.id.my_name);
        myNameTV.setText("My name: " + myName);

        initializeAllImageNote();
        syncImageViewSentCountWithDB();
        initializeSpinner();

    }

    public static Properties getProperties(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("firebase.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    private void displayCount() {

    }

    private void syncImageViewSentCountWithDB() {
        myDataBase.child("notes").get().addOnCompleteListener((task) -> {
            HashMap<String, HashMap<String, String>> tempMap = (HashMap) task.getResult().getValue();
            if (tempMap == null) {
                return;
            }

            for (String entryKey : tempMap.keySet()) {
                String fromUser = tempMap.get(entryKey).get("fromUser");
                if (fromUser != null && fromUser.equals(myName)) {
                    String id = String.valueOf(tempMap.get(entryKey).get("id"));
                    if (id == null) {
                        Log.e("DB-SYNC", "ID not found...");
                        continue;
                    }
                    ImageView imageViewRef = getImageViewById(Integer.parseInt(id));
                    if (imageViewRef == null) {
                        Log.e("DB-SYNC", "ID not supported in this app version...");
                        continue;
                    }
                    imageViewSentCountMap.merge(imageViewRef, 1, Integer::sum);
                }
            }
            displayCount();
        });
    }

    private void initializeSpinner() {
        myDataBase.child("users").get().addOnCompleteListener((task) -> {
            HashMap<String, HashMap<String, String>> tempMap = (HashMap) task.getResult().getValue();
            List<String> userNames = new ArrayList<>();
            if (tempMap == null) {
                return;
            }
            // populate user id and name
            for (String userId : tempMap.keySet()) {
                String userName = tempMap.get(userId).get("username");
                if (userName == null || userName.equals(myName)) {
                    continue;
                }
                userNames.add(userName);
                userIdToUserNameMap.put(userId, userName);
                userNameToUserIdMap.put(userName, userId);
            }
            ArrayAdapter<String> adapter
                    = new ArrayAdapter<>(getApplicationContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    userNames);
            allFriends.setAdapter(adapter);
        });
    }

    private void initializeAllImageNote() {

        for (int i = 1; i < imageNum + 1; i++) {

            int curImageId = getResources().getIdentifier("image" + i,
                    "id", getPackageName());
            ImageView curImage = findViewById(curImageId);
            curImage.setClickable(true);
            imageViewIsClickedMap.put(curImage, false);
            curImage.setOnClickListener((v) -> imageViewOnClickListener(v));
            imageViewSentCountMap.put(curImage, 0);
            imageViewIdToId.put(curImageId, i);

            int curImageTvId = getResources().getIdentifier("image" + i + "SentTimesTV",
                    "id", getPackageName());
            TextView curImageTV = findViewById(curImageTvId);
            imageViewToTextViewMap.put(curImage, curImageTV);

        }
    }

    public void imageViewOnClickListener(View v) {

        if (imageViewIsClickedMap.get(v)) {
            unselectImage((ImageView) v);
        } else {
            // reset all images
            for (ImageView imageView : imageViewIsClickedMap.keySet()) {
                unselectImage(imageView);
            }
            // set current image
            ((ImageView) v).setColorFilter(ContextCompat
                            .getColor(this.getApplicationContext()
                                    , R.color.purple_200)
                    , android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewIsClickedMap.put((ImageView) v, true);
        }
    }

    private void unselectImage(ImageView v) {
        v.setColorFilter(null);
        imageViewIsClickedMap.put(v, false);
    }


    public void createNotificationChannel() {

    }


    private ImageView getImageViewById(int id) {

        if (id > imageNum || id < 1)
            return null;

        int curImageId = getResources().getIdentifier("image" + id,
                "id", getPackageName());
        ImageView curImage = findViewById(curImageId);
        return curImage;
    }
}