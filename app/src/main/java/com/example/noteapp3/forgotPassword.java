package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    private EditText mforgotPassword;
    private Button mpasswordRecoverButton;
    private TextView mgoBackToLogin;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        getSupportActionBar().hide();

        mforgotPassword = findViewById(R.id.forgotPassword);
        mpasswordRecoverButton = findViewById(R.id.passwordRecoverButton);
        mgoBackToLogin = findViewById(R.id.goBackToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mforgotPassword.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                }
                else {
                    // we have to send password recover email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Mail Sent, You can recover your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotPassword.this, MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Email is Wrong or Account Does Not Exist", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });






    }
}