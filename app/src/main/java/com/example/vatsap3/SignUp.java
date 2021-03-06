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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private static String userId;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final ConstraintLayout layout=findViewById(R.id.layout);
        final TextInputEditText email=findViewById(R.id.emailSignUp);
        final TextInputEditText password=findViewById(R.id.passwordSignUp);
        final TextInputEditText adSoyad=findViewById(R.id.adSoyad);
        Button button=findViewById(R.id.button);
        constraintLayout=findViewById(R.id.layout);
        auth=FirebaseAuth.getInstance();
        fDatabase=FirebaseDatabase.getInstance();
        dbRef=fDatabase.getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || adSoyad.getText().toString().isEmpty()){
                    Snackbar.make(layout, "Hiçbir alan boş bırakılamaz", Snackbar.LENGTH_LONG).show();
                } else {
                    if(isNetworkConnected()){
                        createAccount(email.getText().toString(), password.getText().toString(), adSoyad.getText().toString());
                    } else {
                        Snackbar.make(constraintLayout, "Internet Bağlantınızı Kontrol Edin", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private void createAccount(final String email, final String password, final String adSoyad){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser user=auth.getCurrentUser();
                                userId=user.getUid();
                                com.example.vatsap3.FirebaseUser userr=new com.example.vatsap3.FirebaseUser(adSoyad, email, password, userId);
                                String id=dbRef.child("users").push().getKey();
                                dbRef.child("users").push().setValue(userr);
                                Snackbar.make(constraintLayout, "Kayıt başarılı, ana menüye yönlendiriliyor", Snackbar.LENGTH_LONG).show();
                                Timer timer=new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            } else {
                                FirebaseUser user=auth.getCurrentUser();
                                Snackbar.make(constraintLayout, "Kayıt başarısız, ana menüye yönlendiriliyor", Snackbar.LENGTH_LONG).show();
                                Timer timer=new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        }
                    });
    }
}