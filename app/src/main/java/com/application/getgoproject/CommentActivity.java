package com.application.getgoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.application.getgoproject.adapter.CommentAdapter;
import com.application.getgoproject.models.Comment;
import com.application.getgoproject.models.LocationComment;

import java.util.ArrayList;


public class CommentActivity extends AppCompatActivity {
    ListView lvComment;
    ArrayList<LocationComment> arrayComment;
    CommentAdapter adapter;
    private ImageButton imgbtnGoback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);

        anhXa();

        adapter = new CommentAdapter(this, R.layout.layout_cmt, arrayComment);
        lvComment.setAdapter(adapter);
        imgbtnGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeForm();
            }
        });

    }
    private void anhXa(){
        imgbtnGoback = findViewById(R.id.imgbtnGoback);
        lvComment = (ListView) findViewById(R.id.lvComment);
        arrayComment = new ArrayList<>();
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Yen Vi", "2 days ago", "Awesome place for tourist trying to have a sip of coffee. Authentic taste, quiet with beautiful vintage decorations. Recommended.", 1));
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Duc Anh", "a week ago", "Beautiful view, very nice atmostphere, the food so good and cheap. Love it!", 2));
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Mimi", "a week ago", "Nice people, nice view, nice price", 3));
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Yen Vi", "2 days ago", "Awesome place for tourist trying to have a sip of coffee. Authentic taste, quiet with beautiful vintage decorations. Recommended.", 5));
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Duc Anh", "a week ago", "Beautiful view, very nice atmostphere, the food so good and cheap. Love it!", 4));
//        arrayComment.add(new Comment(R.drawable.border_gradient, "Mimi", "a week ago", "Nice people, nice view, nice price", 5));
    }

    private void homeForm(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}