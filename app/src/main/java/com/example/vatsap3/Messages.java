package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;


public class Messages extends AppCompatActivity {

    private FirebaseAuth auth;
    ConstraintLayout constraintLayout;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final TextInputEditText email=findViewById(R.id.email);
        final TextInputEditText password=findViewById(R.id.password);
        constraintLayout=findViewById(R.id.messages);
        Button button=findViewById(R.id.button5);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Snackbar.make(constraintLayout, "Boş kısımları doldurunuz.", Snackbar.LENGTH_LONG).show();
                } else {
                    if(isNetworkConnected()){
                        firebaseAuthWithEmail(email.getText().toString(), password.getText().toString());
                    } else {
                        Snackbar.make(constraintLayout, "Internet Bağlantınızı Kontrol Edin", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void firebaseAuthWithEmail(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                            Intent intent=new Intent(Messages.this, Arayuz.class);
                            intent.putExtra("id", user.getUid());
                            startActivity(intent);
                            } else {
                                Snackbar.make(constraintLayout, "Giriş başarısız", Snackbar.LENGTH_SHORT).show();
                                Timer timer=new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(Messages.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                        }
                    }
                });
    }
}