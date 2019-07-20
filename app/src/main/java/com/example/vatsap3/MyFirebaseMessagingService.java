package com.example.vatsap3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        System.out.println(token);
                    }
                });
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        System.out.println(s);


    }

    public void sendNotification(String message, User you, String context){


        NotificationManager notificationManager= (NotificationManager)getSystemService(context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){   //BİLDİRİM İÇİN CHANNEL GEREKİYOR
            CharSequence name="bildirim";
            String description="bildirim";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("ID", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder;

        builder=new NotificationCompat.Builder(this, "ID") //BİLDİRİM İÇİN GEREKLİ
                .setSmallIcon(R.drawable.ic_launcher_foreground)    //MESAJIN BİLDİRİMDE GÖRÜNMESİ İÇİN İÇERDE TANIMLADIM
                .setContentTitle(you.getName())
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        final NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(0, builder.build());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
