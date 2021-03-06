package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class Chat extends AppCompatActivity {

    ChatView chatView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    User me;
    User you;
    int position;
    String id;
    String youId;
    ConstraintLayout constraintLayout;
    String message;
    Message message2;
    int sayac;
    String packageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        packageName=getApplicationContext().getPackageName();

        chatView=findViewById(R.id.chat_view);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference2=FirebaseDatabase.getInstance().getReference();
        constraintLayout=findViewById(R.id.layout);

        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        chatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.colorAccent));
        chatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.WHITE);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);

        Intent intent=getIntent();
        position=intent.getIntExtra("position", 0);
        id=intent.getStringExtra("id");
        youId=intent.getStringExtra("youId");
        sayac=intent.getIntExtra("sayac",0);
        String isim=intent.getStringExtra("you");
        me=new User(id, "You");
        you=new User(youId, isim);

        detect();

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Message message1 = new Message.Builder()
                        .setUser(me) // Sender
                        .setRight(true) // This message Will be shown right side.
                        .setText(chatView.getInputText()) //Message contents
                        .build();



                chatView.setInputText("");
                String message=message1.getText();
                writeChat(id, youId, message);
                chatView.send(message1);

            }
        });
    }

    public void writeChat(String meId,String youId, String message){
        ChatDB chat=new ChatDB(message);
        databaseReference.child("messages").child(meId+"_"+ youId).setValue(chat);
    }

    public void sendNotification(){
        NotificationCompat.Builder builder;

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){   //BİLDİRİM İÇİN CHANNEL GEREKİYOR
            CharSequence name="bildirim";
            String description="bildirim";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("ID", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        final NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);

        builder=new NotificationCompat.Builder(Chat.this, "ID") //BİLDİRİM İÇİN GEREKLİ  MESAJIN BİLDİRİMDE GÖRÜNMESİ İÇİN İÇERDE TANIMLADIM
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(you.getName())
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        notificationManagerCompat.notify(0, builder.build());


    }

    public void detect(){
        databaseReference2.child("messages").child(youId+"_"+id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(Chat.this,"Bir sorun oluştu", Toast.LENGTH_SHORT).show();
                } else {
                    if(sayac != 0){
                        ChatDB chatDB=dataSnapshot.getValue(ChatDB.class);
                        message=chatDB.getMessage();
                        message2 = new Message.Builder()
                                .setUser(you)
                                .setRight(false)
                                .setText(message)
                                .build();
                        chatView.receive(message2);

                        if(!isAppOnForeground(getApplicationContext(), packageName)){
                            sendNotification();
                        }

                    }
                    sayac++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    //PROGRAMIN ARKAPLANDA OLUP OLMADIĞINI TESPİT ETMEK İÇİN GEREKLİ FONKSİYON. STACKOVERFLOW'DAN (Ç)ALDIM.

    private boolean isAppOnForeground(Context context,String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}