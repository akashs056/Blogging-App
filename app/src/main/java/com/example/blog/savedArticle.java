package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blog.databinding.ActivityReadMoreBinding;
import com.example.blog.databinding.ActivitySavedArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class savedArticle extends AppCompatActivity {
    ActivitySavedArticleBinding binding;
    ArrayList<postModel> savedList;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        savedList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(savedArticle.this);
        binding.savedRV.setLayoutManager(layoutManager);

        database.getReference().child("Users").child(auth.getUid())
                .child("savedPost").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String postId = dataSnapshot.getKey();
                            if (postId != null) {
                                database.getReference().child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        postModel post = snapshot.getValue(postModel.class);
                                        if (post!=null) {
                                            post.setSavedPostId(postId);
                                            savedList.add(post);
                                        }
                                        Collections.reverse(savedList);

                                        savedAdapter adapter = new savedAdapter(savedList, savedArticle.this);
                                        binding.savedRV.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
