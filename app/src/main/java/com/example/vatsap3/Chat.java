package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Chat extends AppCompatActivity  {

    ChatView chatView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
//    ArrayList<String> arrayList;
    User me;
    User you;
    String mee;
    int position;
    String id;
    String youId;
    ConstraintLayout constraintLayout;
    String message;
    Message message2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView=findViewById(R.id.chat_view);
//        arrayList=new ArrayList<>();
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
        String isim=intent.getStringExtra("you");
        me=new User(id, "You");
        you=new User(youId, isim);

        databaseReference2.child("messages").child(youId+"_"+id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatDB chatDB=dataSnapshot.getValue(ChatDB.class);
                message=chatDB.getMessage();
                message2 = new Message.Builder()
                        .setUser(you)
                        .setRight(false)
                        .setText(message)
                        .build();
                chatView.receive(message2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

}