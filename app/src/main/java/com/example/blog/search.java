package com.example.blog;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class search extends AppCompatActivity {
    ActivitySearchBinding binding;
    SearchAdapter searchAdapter;
    ArrayList<postModel> searchList;
    RecyclerView rvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchBlog.requestFocus();

        searchList = new ArrayList<>();
        searchAdapter = new SearchAdapter(searchList, this);
        rvSearch = findViewById(R.id.rv_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(search.this, LinearLayoutManager.VERTICAL, false);
        rvSearch.setLayoutManager(layoutManager);
        rvSearch.setAdapter(searchAdapter);

        binding.goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add TextWatcher for search functionality
        binding.searchBlog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list based on search input
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void performSearch(String query) {
        searchList.clear();
        if (!query.isEmpty()) {
            String lowercaseQuery = query.toLowerCase();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {// Convert query to lowercase
                @Override
                public void run() {
                    FirebaseDatabase.getInstance().getReference().child("posts")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Set<String> uniquePostIds = new HashSet<>();
                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        // Traverse to the child containing postTitle
                                        String postTitle = postSnapshot.child("postTitle").getValue(String.class);
                                        if (postTitle != null) {
                                            String postTitleLower = postTitle.toLowerCase();
                                            if (postTitleLower.startsWith(lowercaseQuery) && !uniquePostIds.contains(postSnapshot.getKey())) {
                                                postModel post = postSnapshot.getValue(postModel.class);
                                                if (post != null) {
                                                    post.setPostId(postSnapshot.getKey());
                                                    searchList.add(post);
                                                    uniquePostIds.add(postSnapshot.getKey());
                                                    Log.d("SearchDebug", "Added post: " + post.getPostTitle());
                                                }
                                            }
                                        }
                                    }
                                    searchAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("SearchDebug", "Error: " + error.getMessage());
                                }
                            });
                }
            },500);
        } else {
            searchAdapter.notifyDataSetChanged();
        }
    }
}
