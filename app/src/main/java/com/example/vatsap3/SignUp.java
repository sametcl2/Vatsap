package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private String userId;
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
                if(email.toString().isEmpty() || password.toString().isEmpty() || adSoyad.toString().isEmpty()){
                    Snackbar.make(layout, "Hiçbir alan boş bırakılamaz", Snackbar.LENGTH_LONG).show();
                } else {
                    createAccount(email.getText().toString(), password.getText().toString());
                    writeNewUser(email.getText().toString(), password.getText().toString(), adSoyad.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user=auth.getCurrentUser();
                                userId=user.getUid();
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

    private void writeNewUser(String eMail, String password, String adSoyad){
        User user=new User(adSoyad, eMail, password);
        dbRef.child("users").push().setValue(user);
    }
}