package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createNote extends AppCompatActivity {

    EditText mcreateContentOfNote, mcreateTitleOfNote;
    FloatingActionButton msaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressBarOfCreatNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);


        msaveNote = findViewById(R.id.saveNote);
        mcreateContentOfNote = findViewById(R.id.createContentOfNote);
        mcreateTitleOfNote = findViewById(R.id.createTitleOfNote);

        mprogressBarOfCreatNote = findViewById(R.id.progressBarOfCreatNote);

        Toolbar toolbar = findViewById(R.id.toolBarOfCreateNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();



        msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mcreateTitleOfNote.getText().toString();
                String content = mcreateContentOfNote.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both field are Require", Toast.LENGTH_SHORT).show();
                }
                else {
                          // Store data on firebase or firestore

                    mprogressBarOfCreatNote.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getApplicationContext(),"Note Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createNote.this, notesActivity.class));

                            
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Create Note", Toast.LENGTH_SHORT).show();

                            mprogressBarOfCreatNote.setVisibility(View.INVISIBLE);
                        }
                    });






                }
            }
        });






    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}