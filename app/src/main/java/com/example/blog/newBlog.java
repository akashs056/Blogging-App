package com.example.blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.blog.databinding.ActivityNewBlogBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class newBlog extends AppCompatActivity {

    ActivityNewBlogBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        dialog=new ProgressDialog(newBlog.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait......");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userModel user = snapshot.getValue(userModel.class);
                    if (user != null) {
                        Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.placeholder).into(binding.profileimage);
                        binding.name.setText(user.getName());
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

        binding.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description=binding.description.getText().toString();
                if (!description.isEmpty()){
                    binding.post.setTextColor(newBlog.this.getResources().getColor(R.color.white));
                    binding.post.setBackgroundDrawable(ContextCompat.getDrawable(newBlog.this,R.drawable.follow_btbn_bg));
                    binding.post.setEnabled(true);
                }else {
                    binding.post.setTextColor(newBlog.this.getResources().getColor(R.color.gray));
                    binding.post.setBackgroundDrawable(ContextCompat.getDrawable(newBlog.this,R.drawable.follow_active));
                    binding.post.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                postModel post=new postModel();
                post.setPostedBy(auth.getUid());
                post.setPostTitle(binding.title.getText().toString());
                post.setPostDescription(binding.description.getText().toString());
                post.setPostedAt(new Date().getTime());

                database.getReference().child("posts")
                        .push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(newBlog.this, "Blog Added Successfully", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        });
                database.getReference().child("Users").child(auth.getUid()).child("blogs").push()
                        .setValue(post);
            }
        });
        
    }
}