package com.example.noteapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class noteDetails extends AppCompatActivity {


    private TextView mTitleOfNoteDetail, mContentOfNoteDetail;
    FloatingActionButton mgoToEditeNote;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        mContentOfNoteDetail = findViewById(R.id.ContentOfNoteDetail);
        mTitleOfNoteDetail = findViewById(R.id.TitleOfNoteDetail);
        mgoToEditeNote = findViewById(R.id.goToEditeNote);
        Toolbar toolbar = findViewById(R.id.toolBarOfNoteDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.backGround);
        imageView.setImageResource(getRandomImageBackground());
        Intent data = getIntent();

        mgoToEditeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), editNoteActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);

            }
        });


        mContentOfNoteDetail.setText(data.getStringExtra("content"));
        mContentOfNoteDetail.setText(data.getStringExtra("title"));

    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRandomImageBackground() {
        int[] imageViews = {R.drawable.img1, R.drawable.img2, R.drawable.img4, R.drawable.img5};

        Random random=new Random();
        int number=random.nextInt(imageViews.length);
        return imageViews[number];
    }

}