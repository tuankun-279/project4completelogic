package tuan.aprotrain.projectpetcare.entity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import tuan.aprotrain.projectpetcare.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        String CHANNEL_ID = "MESSAGE";
        CharSequence name;
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Message Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.gigapet)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notification.build());
        //insert a fake record to Firebase Realtime DB
        super.onMessageReceived(message);
    }
}
