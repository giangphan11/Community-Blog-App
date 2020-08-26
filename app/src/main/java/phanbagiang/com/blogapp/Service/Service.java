package phanbagiang.com.blogapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import phanbagiang.com.blogapp.Activities.MainActivity;
import phanbagiang.com.blogapp.R;

public class Service extends FirebaseMessagingService {
    private static final String TAG = "Service";
    public static final String CHANEL_ID="chanel11";
    public static final int ID=11;
    private Notification notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANEL_ID,"ch1 by GP",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Giang Phan dev");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        // create notifi
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANEL_ID)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_ex)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setWhen(System.currentTimeMillis());
//
//
        notificationManagerCompat.notify(ID,builder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }

}
