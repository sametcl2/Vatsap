package com.example.vatsap3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.IChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.android.material.snackbar.Snackbar;

public class Chat extends AppCompatActivity  {

    ChatView chatView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView=findViewById(R.id.chat_view);
        final LinearLayout layout=findViewById(R.id.layout2);


        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        chatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.colorAccent));
        chatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.WHITE);

        final User me = new User("0", "Samet");
        final User you = new User("1", "Şahin");

        final Message message1 = new Message.Builder()
                .setUser(me) // Sender
                .setRight(true) // This message Will be shown right side.
                .setText("Hello") //Message contents
                .build();
        final Message message2 = new Message.Builder()
                .setUser(you) // Sender
                .setRight(false) // This message Will be shown left side.
                .setText("What's up?") //Message contents
                .build();

                chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatView.send(message1); // Will be shown right side
                chatView.receive(message2); // Will be shown left side
            }
        });

    }
}
