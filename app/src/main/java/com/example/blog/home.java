package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blog.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class home extends AppCompatActivity {
    ArrayList<postModel> list;
    ActivityHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ShimmerRecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.blogRV);
        recyclerView.showShimmer();
        list=new ArrayList<>();

        LinearLayoutManager layoutManager=new LinearLayoutManager(home.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        database.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        postModel post=dataSnapshot.getValue(postModel.class);
                        post.setPostId(dataSnapshot.getKey());
                        list.add(post);
                    }

                    Collections.reverse(list);
                    homeAdapter adapter =new homeAdapter(list,home.this);
                    binding.blogRV.setAdapter(adapter);
                    binding.blogRV.hideShimmer();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, search.class);
                startActivity(intent);
            }
        });

        binding.saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(home.this,savedArticle.class);
                startActivity(intent);
            }
        });

        binding.addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(home.this,newBlog.class);
                startActivity(intent);
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userModel user=snapshot.getValue(userModel.class);
                    Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.placeholder).into(binding.profileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(home.this,proflePage.class);
                startActivity(intent);
            }
        });



    }
}