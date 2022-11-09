package edu.northeastern.group33webapi.Firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.northeastern.group33webapi.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String username;
    private String userId;
    private DatabaseReference myDataBase;
    private static final String TAG = "mFirebaseIIDService";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";

    private static final Map<Integer, Integer> idToDrawable = new HashMap<>();

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        myDataBase = FirebaseDatabase.getInstance().getReference();
        userId = Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        // **** username
        myDataBase.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                HashMap tempMap = (HashMap) task.getResult().getValue();
                if (tempMap == null) {
                    Log.e("firebase", "Empty data");
                    return;
                }
                username = Objects.requireNonNull(tempMap.get("username")).toString();
            }
        });
        idToDrawable.put(1, R.drawable.sun);
        idToDrawable.put(2, R.drawable.rain);
        idToDrawable.put(3, R.drawable.rainstorm);
        idToDrawable.put(4, R.drawable.snow);
        idToDrawable.put(5, R.drawable.wind);
        idToDrawable.put(6, R.drawable.cloud);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
        System.out.println("onTokenRefresh completed with token: " + token);

        @SuppressLint("HardwareIds") User user = new User(this.username, Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), token);
        myDataBase.child("users").child(user.username).setValue(user);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        myClassifier(remoteMessage);
        Log.e("msgId", remoteMessage.getMessageId());
        Log.e("senderId", remoteMessage.getSenderId());
    }

    private void myClassifier(RemoteMessage remoteMessage) {

        // https://firebase.google.com/docs/reference/android/com/google/firebase/messaging/RemoteMessage
        String identificator = remoteMessage.getFrom();
        if (identificator != null) {
            if (identificator.contains("topic")) {
                if (remoteMessage.getNotification() != null) {
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    showNotification(remoteMessage.getNotification());
                    // postToastMessage(notification.getTitle(), getApplicationContext());
                }
            } else {
                if (remoteMessage.getData().size() > 0) {
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    assert notification != null;
                    showNotification(notification, Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("image_id"))));
                    // postToastMessage(remoteMessage.getData().get("title"), getApplicationContext());
                }
            }
        }
    }

    public static void postToastMessage(final String message, final Context context){
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }

    private void showNotification(RemoteMessage.Notification remoteMessageNotification, int imageId) {
        System.out.println("start processing show notification");
        Intent intent = new Intent(this, SendNoteToFriendsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        System.out.println("notification title: " + remoteMessageNotification.getTitle());

        // building and sending notification

        Bitmap bm = BitmapFactory.decodeResource(getResources(), idToDrawable.get(imageId));
        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessageNotification.getTitle())
                .setContentText(remoteMessageNotification.getBody())
                .setLargeIcon(bm)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bm)
                        .bigLargeIcon(null))
                .addAction(R.mipmap.ic_launcher, "Snooze", pendingIntent)
                .build();
        notificationManager.notify(0, notification);

    }

    private void showNotification(RemoteMessage.Notification remoteMessageNotification) {
        System.out.println("start processing show notification");
        Intent intent = new Intent(this, SendNoteToFriendsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        System.out.println("notification title: " + remoteMessageNotification.getTitle());

        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessageNotification.getTitle())
                .setContentText(remoteMessageNotification.getBody())
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0, notification);

    }
}