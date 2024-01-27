package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.blog.databinding.ActivityYourArticlesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class yourArticles extends AppCompatActivity {

    ActivityYourArticlesBinding binding;
    ArrayList<postModel> yourList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityYourArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        yourList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(yourArticles.this);
        binding.yourRV.setLayoutManager(layoutManager);

//        database.getReference().child("Users").child(auth.getUid()).child("blogs").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                yourList.clear();
//                if (snapshot.exists()){
//                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                        postModel model=dataSnapshot.getValue(postModel.class);
//                        yourList.add(model);
//                    }
//                    yourAdapter adapter=new yourAdapter(yourList,yourArticles.this);
//                    binding.yourRV.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        database.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        postModel model = dataSnapshot.getValue(postModel.class);
                        model.setPostId(dataSnapshot.getKey());
                        if (model != null && model.getPostedBy() != null && model.getPostedBy().equals(auth.getUid())) {
                            yourList.add(model);
                        }
                    }
                    Collections.reverse(yourList);
                    yourAdapter adapter = new yourAdapter(yourList, yourArticles.this);
                    binding.yourRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
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