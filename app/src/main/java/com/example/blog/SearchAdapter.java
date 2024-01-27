package com.example.blog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blog.databinding.SampleRvBinding;
import com.example.blog.databinding.SearchRvBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<postModel> searchList;
    private Context context;

    public SearchAdapter(ArrayList<postModel> searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        postModel post = searchList.get(position);

        holder.binding.searchtxt.setText(post.getPostTitle());

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
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
                        intent.putExtra("postId",post.getPostId());
                        intent.putExtra("postLike",post.getPostLike());
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SearchRvBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchRvBinding.bind(itemView);
        }
    }
}
