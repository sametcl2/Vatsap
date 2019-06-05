package com.example.vatsap3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputEditText email=findViewById(R.id.emailSignUp);
        final TextInputEditText password=findViewById(R.id.passwordSignUp);
        Button button=findViewById(R.id.button);

        auth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(email.getText().toString(), password.getText().toString());
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
                                Toast.makeText(SignUp.this, "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                                //TODO YENİ ACTİVİTY'YE GEÇİŞ
                            } else {
                                FirebaseUser user=auth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Kayıt Başarısız", Toast.LENGTH_LONG).show();
                                //TODO ESKİ ACTİVİTY'YE GEÇİŞ
                            }
                        }
                    });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        Toast.makeText(SignUp.this, "Zaten kayitlisiniz", Toast.LENGTH_SHORT).show();
    }
}