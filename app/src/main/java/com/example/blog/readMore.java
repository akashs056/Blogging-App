
package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.blog.databinding.ActivityReadMoreBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class readMore extends AppCompatActivity {
    ActivityReadMoreBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReadMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String postId=intent.getStringExtra("postId");
        String postLike=intent.getStringExtra("postLike");
        if (user!=null && !user.isEmpty()){
            database.getReference().child("Users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userModel model = snapshot.getValue(userModel.class);
                        Picasso.get().load(model.getProfile_photo()).placeholder(R.drawable.placeholder).into(binding.profileimage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.heading.setText(intent.getStringExtra("heading"));
        binding.body.setText(intent.getStringExtra("body"));
        long time = intent.getLongExtra("time", 0);
        String timme= TimeAgo.using(time);
        binding.time.setText(timme);
        binding.name.setText(intent.getStringExtra("name"));

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        database.getReference().child("posts").child(postId).child("likes").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.like.setImageResource(R.drawable.like);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.like.setImageResource(R.drawable.like);
                int currentLikeCount = (postLike == null) ? 0 : Integer.parseInt(postLike);
                FirebaseDatabase.getInstance().getReference().child("posts")
                        .child(postId).child("likes").child(FirebaseAuth.getInstance().getUid())
                        .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference().child("posts")
                                        .child(postId).child("postLike").setValue(currentLikeCount+1);
                            }
                        });
            }
        });



    }
}