package com.example.vatsap3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.android.material.snackbar.Snackbar;

public class Chat extends AppCompatActivity {

    ChatView chatView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView=findViewById(R.id.chat_view);
        final LinearLayout layout=findViewById(R.id.layout2);

        String myName="Samet";
        String yourName="Hebele Hübele";

        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        chatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.colorAccent));
        chatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.WHITE);

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ÇALIŞIYOR");
            }
        });

    }
}
