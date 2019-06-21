package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.IChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Chat extends AppCompatActivity  {

    ChatView chatView;
    DatabaseReference databaseReference;
    ArrayList<String> arrayList;
    User me;
    User you;
    String mee;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView=findViewById(R.id.chat_view);
        arrayList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference();

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
        String id=intent.getStringExtra("you");
        me=new User("0", "You");
        you=new User("1",id);

            chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Message message1 = new Message.Builder()
                        .setUser(me) // Sender
                        .setRight(true) // This message Will be shown right side.
                        .setText(chatView.getInputText()) //Message contents
                        .build();

                final Message message2 = new Message.Builder()
                        .setUser(me) // Sender
                        .setRight(true) // This message Will be shown right side.
                        .setText(databaseReference.child("messages").child(arrayList.get(position)).child("lastMessage").toString()) //Message contents
                        .build();

                chatView.setInputText("");
                writeChat(chatView.getInputText());
                chatView.send(message1);
                chatView.receive(message2);

            }
        });
    }

    public void writeChat(String lastMessage){
        ChatDB chat=new ChatDB(lastMessage);
        databaseReference.child("messages").child(arrayList.get(position)).setValue(chat);
    }

}