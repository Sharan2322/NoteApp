 package com.example.noteapp3;

 import android.content.Intent;
 import android.os.Bundle;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.EditText;
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

 public class editNoteActivity extends AppCompatActivity {
    
    Intent data;
    EditText meditTitleOfNote, meditContentOfNote;
    FloatingActionButton msaveEditNote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        
        meditContentOfNote = findViewById(R.id.editContentOfNote);
        meditTitleOfNote = findViewById(R.id.editTitleOfNote);
        msaveEditNote = findViewById(R.id.saveEditNote);

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolBarOfEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        msaveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "saveButton click", Toast.LENGTH_SHORT).show();


                 String newtitle = meditTitleOfNote.getText().toString();
                 String newContent = meditContentOfNote.getText().toString();
                 
                 if (newContent.isEmpty() || newtitle.isEmpty())
                 {
                     Toast.makeText(getApplicationContext(), "Something is empty", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 else {
                     DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNote").document(data.getStringExtra("noteId"));
                     Map<String, Object> note = new HashMap<>();
                     note.put("title", newtitle);
                     note.put("content", newContent);
                     documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(getApplicationContext(), "Note is update", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(editNoteActivity.this, notesActivity.class));
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(getApplicationContext(), "Failed To update", Toast.LENGTH_SHORT).show();

                         }
                     });

                 }
            }
        });


        String  notetitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        meditTitleOfNote.setText(notetitle);
        meditTitleOfNote.setText(noteContent);

        
    }

     public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId() == android.R.id.home){
             onBackPressed();
         }

         return super.onOptionsItemSelected(item);
     }



}