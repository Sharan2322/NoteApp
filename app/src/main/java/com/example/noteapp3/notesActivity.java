package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesActivity extends AppCompatActivity {

    FloatingActionButton mcreateNotesFab;
    private FirebaseAuth firebaseAuth;


    RecyclerView mrecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebaseModle, NoteViewHolder> noteAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        mcreateNotesFab = findViewById(R.id.createNotesFab);
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getSupportActionBar().setTitle("All Notes");


        mcreateNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notesActivity.this, createNote.class));
            }
        });


        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebaseModle> allUserNotes = new FirestoreRecyclerOptions.Builder<firebaseModle>().setQuery(query, firebaseModle.class).build();




        noteAdapter = new FirestoreRecyclerAdapter<firebaseModle, NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebaseModle model) {

                ImageView popUpButton = holder.itemView.findViewById(R.id.menupopButton);

                // set colors
                int colorCode = getRandomColor();
                holder.mNote.setBackgroundColor(holder.itemView.getResources().getColor(colorCode,null));

                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // we have to open note detail activity

                        Intent intent = new Intent(view.getContext(), noteDetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noteId", docId);


                        view.getContext().startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "This is Clicked", Toast.LENGTH_SHORT).show();

                    }
                });


                popUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);     // error :-  mrecycleView ---> v
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {


                                Intent intent = new Intent(view.getContext(), editNoteActivity.class);
                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("content", model.getContent());
                                intent.putExtra("noteId", docId);

                                view.getContext().startActivity(intent);
                                return false;
                            }
                        });


                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                                Toast.makeText(getApplicationContext(), "This note is deleted", Toast.LENGTH_SHORT).show();

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);

                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "This note is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return false;
                            }
                        });

                        popupMenu.show();

                    }
                });



            }












            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };




        mrecyclerView = findViewById(R.id.recyclerView);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(noteAdapter);





    }





    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout mNote;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            mNote = itemView.findViewById(R.id.note);


        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (noteAdapter != null){
            noteAdapter.stopListening();
        }
    }




    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.gray);
        colorCode.add(R.color.green);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.lightgreen);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int number = random.nextInt(colorCode.size());
        return colorCode.get(number);

    }


}