package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private EditText mloginEmail, mloginPassword;
    private RelativeLayout mlogin, mgoToSignUp;
    private TextView mgoToForgotPassword;

    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressBarOfMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar().hide();

        mloginEmail = findViewById(R.id.loginEmail);
        mloginPassword = findViewById(R.id.loginPassword);
        mlogin = findViewById(R.id.login);
        mgoToSignUp = findViewById(R.id.goToSignUp);
        mgoToForgotPassword = findViewById(R.id.goToForgotPassword);
        mprogressBarOfMainActivity = findViewById(R.id.progressBarOfMainActivity);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser != null){
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        }





        mgoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // short cut for new Intent
                startActivity(new Intent(MainActivity.this, signUp.class));
            }
        });

        mgoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, forgotPassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginEmail.getText().toString().trim();
                String password = mloginPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Field are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Login the user

                    mprogressBarOfMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                checkEmailVerification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }
            }
        });




    }



    private void checkEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified() == true){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        }
        else {
            mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your mail first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }




}