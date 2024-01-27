package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blog.databinding.ActivityEditArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class editArticle extends AppCompatActivity {
    ActivityEditArticleBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        Intent intent=getIntent();
        String postId=intent.getStringExtra("postId");
        binding.title.setText(intent.getStringExtra("heading"));
        binding.description.setText(intent.getStringExtra("description"));

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("posts").child(postId).child("postTitle").setValue(binding.title.getText().toString());
                database.getReference().child("posts").child(postId).child("postDescription").setValue(binding.description.getText().toString());
                Toast.makeText(editArticle.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}