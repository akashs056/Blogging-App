package com.example.blog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.databinding.YourSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class yourAdapter extends RecyclerView.Adapter<yourAdapter.viewholder> {
    
    ArrayList<postModel> list;
    Context context;

    public yourAdapter(ArrayList<postModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.your_sample,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        postModel postModel=list.get(position);
        holder.binding.heading.setText(postModel.getPostTitle());
        holder.binding.body.setText(postModel.getPostDescription());
        String time= TimeAgo.using(postModel.getPostedAt());
        holder.binding.time.setText(time);

        holder.binding.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,readMore.class);
                intent.putExtra("heading",postModel.getPostTitle());
                intent.putExtra("body",postModel.getPostDescription());
                intent.putExtra("time",postModel.getPostedAt());
                FirebaseDatabase.getInstance().getReference().child("Users").child(postModel.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel user=snapshot.getValue(userModel.class);
                        intent.putExtra("name",user.getName());
                        intent.putExtra("user",postModel.getPostedBy());
                        intent.putExtra("postId",postModel.getPostId());
                        intent.putExtra("postLike",postModel.getPostLike());
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, editArticle.class);
                intent.putExtra("heading",postModel.getPostTitle());
                intent.putExtra("description",postModel.getPostDescription());
                intent.putExtra("postId",postModel.getPostId());
                context.startActivity(intent);
            }
        });
        
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("posts").child(postModel.getPostId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Blog deleted Successfully", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel user=snapshot.getValue(userModel.class);
                        Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.placeholder).into(holder.binding.profileimage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        YourSampleBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding=YourSampleBinding.bind(itemView);
        }
    }
}
