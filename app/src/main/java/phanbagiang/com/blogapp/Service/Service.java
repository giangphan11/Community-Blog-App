package phanbagiang.com.blogapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import phanbagiang.com.blogapp.R;

public class Service extends FirebaseMessagingService {
    private static final String TAG = "Service";
    public static final String CHANEL_ID="chanel11";
    public static final int ID=1;
    private Notification notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANEL_ID,"Notifiacation",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Giang Phan dev");
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableLights(true);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            if(notificationManager!=null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        // create notifi
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANEL_ID)
                .setSmallIcon(R.drawable.ic_ex)
                .setContentText(body)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis());

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
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
