package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputEditText email=findViewById(R.id.emailSignUp);
        final TextInputEditText password=findViewById(R.id.passwordSignUp);
        final TextInputEditText adSoyad=findViewById(R.id.adSoyad);
        Button button=findViewById(R.id.button);

        auth=FirebaseAuth.getInstance();
        fDatabase=FirebaseDatabase.getInstance();
        dbRef=fDatabase.getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(email.getText().toString(), password.getText().toString());
                writeNewUser(email.getText().toString(), password.getText().toString(), adSoyad.getText().toString());
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
                                Toast.makeText(SignUp.this, "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SignUp.this, Arayuz.class);
                                startActivity(intent);
                            } else {
                                FirebaseUser user=auth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Kayıt Başarısız", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
    }

    private void writeNewUser(String eMail, String password, String adSoyad){
        User user=new User(adSoyad, eMail, password);
        dbRef.child("users").push().setValue(user);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        Toast.makeText(SignUp.this, "Zaten kayitlisiniz", Toast.LENGTH_SHORT).show();
        //FirebaseAuth.getInstance().signOut();
    }
}