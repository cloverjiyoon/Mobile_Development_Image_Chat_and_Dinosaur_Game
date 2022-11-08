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
        for (ImageView imageView : imageViewToTextViewMap.keySet()) {
            TextView textView = imageViewToTextViewMap.get(imageView);
            textView.setText("Times sent: " + imageViewSentCountMap.get(imageView));
        }
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
        // This must be called early because it must be called before a notification is sent.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void onHistoryButtonPressed(View v) {
        Intent intent = new Intent(this, NoteReceivedHistoryActivity.class);
        intent.putExtra("user_name", myName);
        startActivity(intent);
    }

    public void onSubmitButtonPressed(View v) {
        String selectedUsername = allFriends.getSelectedItem().toString();
        int selectedImageId = getCurrentSelectedId();
        if (selectedImageId == -1) {
            Context context = getApplicationContext();
            CharSequence text = "no note is selected for" + selectedUsername;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        Note note = new Note(selectedImageId, myName, selectedUsername, start());

        // Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bean_stew);
        myDataBase.child("note").child(note.getKey()).setValue(note).addOnSuccessListener(
                (task) -> {
                    Context context_success = getApplicationContext();
                    CharSequence text_success = "note successfully send to " + selectedUsername;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context_success, text_success, duration);
                    toast.show();

                    ImageView imageViewById = getImageViewById(selectedImageId);
                    if (imageViewById == null) {
                        Log.e("UI", "ID not supported in this app version...");
                        return;
                    }
                    imageViewSentCountMap.merge(getImageViewById(selectedImageId), 1, Integer::sum);
                    displayCount();
                });
        myDataBase.child("users").child(userNameToUserIdMap.get(selectedUsername)).get().addOnCompleteListener((task) -> {
            HashMap tempMap = (HashMap) task.getResult().getValue();

            // username instead of Token?
            String token = tempMap.get("token").toString();

            new Thread(() -> sendMessageToDevice(token, note)).start();
        });
    }

    private void sendMessageToDevice(String targetToken, Note note) {
        System.out.println("client token:" + targetToken);
        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            String title = "note from " + note.fromUser;
            String msg = "this is a Note " + note.id;
            jNotification.put("title", title);
            jNotification.put("body", msg);
            jdata.put("title", "data:" + title);
            jdata.put("content", "data:" + msg);
            jdata.put("image_id", note.id);

            jPayload.put("to", targetToken);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String resp = fcmHttpConnection(SERVER_KEY, jPayload);
        // postToastMessage("Status from Server: " + resp, getApplicationContext());

    }

    private static void postToastMessage(final String message, final Context context){
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private static String fcmHttpConnection(String serverToken, JSONObject jsonObject) {
        try {

            // Open the HTTP connection and send the payload
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", serverToken);
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            return "NULL";
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        return "";
    }

    private String getCurrentUsername() {
        return userIdToUserNameMap.get(Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    private ImageView getSelectedImage() {
        for (Map.Entry<ImageView, Boolean> entry : imageViewIsClickedMap.entrySet()) {
            if (entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String start() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    private ImageView getImageViewById(int id) {

        if (id > imageNum || id < 1)
            return null;

        int curImageId = getResources().getIdentifier("image" + id,
                "id", getPackageName());
        ImageView curImage = findViewById(curImageId);
        return curImage;
    }

    @SuppressLint("NonConstantResourceId")
    public int getCurrentSelectedId() {
        return -1;
    }
}