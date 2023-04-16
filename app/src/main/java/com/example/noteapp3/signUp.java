package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUp extends AppCompatActivity {

    private EditText msignUpEmail, msignUpPassword;
    private RelativeLayout msignUp;
    private TextView mgoToLogIn;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        getSupportActionBar().hide();

        msignUp = findViewById(R.id.signUp);
        msignUpEmail = findViewById(R.id.signUpEmail);
        msignUpPassword = findViewById(R.id.signUpPassword);
        mgoToLogIn = findViewById(R.id.goToLogIn);

        firebaseAuth = FirebaseAuth.getInstance();

        mgoToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = msignUpEmail.getText().toString().trim();
                String password = msignUpPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(signUp.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(signUp.this, "Password Should Greater than 6 Digits", Toast.LENGTH_SHORT).show();
                }
                else {
                    // registered the user to firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Failed To Register", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }

            }
        });


    }




    // send emil verification
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email is Sent, Verify and Log In Agian", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signUp.this, MainActivity.class));
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Failed To Send Verification Email", Toast.LENGTH_SHORT).show();
        }

    }


}
