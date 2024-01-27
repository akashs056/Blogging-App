package com.example.blog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.databinding.SampleRvBinding;
import com.example.blog.databinding.SavedSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class savedAdapter extends  RecyclerView.Adapter<savedAdapter.viewHolder> {

    ArrayList<postModel> list;
    Context context;
    public savedAdapter(ArrayList<postModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.saved_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        postModel post=list.get(position);
            holder.binding.heading.setText(post.getPostTitle());
            holder.binding.body.setText(post.getPostDescription());
            String time= TimeAgo.using(post.getPostedAt());
            holder.binding.time.setText(time);
            holder.binding.like.setText(post.getPostLike() + "");

        holder.binding.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,readMore.class);
                intent.putExtra("heading",post.getPostTitle());
                intent.putExtra("body",post.getPostDescription());
                intent.putExtra("time",post.getPostedAt());
                FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel user=snapshot.getValue(userModel.class);
                        intent.putExtra("name",user.getName());
                        intent.putExtra("user",post.getPostedBy());
                        intent.putExtra("postId",post.getSavedPostId());
                        intent.putExtra("postLike",post.getPostLike());
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userModel user = snapshot.getValue(userModel.class);
                        holder.binding.like.setText(post.getPostLike() + "");
                        Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.placeholder).into(holder.binding.profileimage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("posts").child(post.getSavedPostId()).child("likes")
                    .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                holder.binding.like.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.like, 0);
                                holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.binding.like.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.non_like, 0);
                                        holder.binding.like.setText(post.getPostLike()-1+"");
                                        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getSavedPostId()).
                                                child("likes").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getSavedPostId())
                                                                .child("postLike").setValue(post.getPostLike()-1);
                                                    }
                                                });
                                    }
                                });
                            }else {
                                holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.binding.like.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.like, 0);
                                        holder.binding.like.setText(post.getPostLike()+1+"");
                                        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getSavedPostId()).
                                                child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getSavedPostId())
                                                                .child("postLike").setValue(post.getPostLike()+1);
                                                    }
                                                });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            holder.binding.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.binding.save.setImageResource(R.drawable.save);
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getUid()).child("savedPost")
                            .child(post.getSavedPostId()).removeValue();
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        SavedSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SavedSampleBinding.bind(itemView);
        }
    }
}
