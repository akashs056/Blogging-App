package com.example.blog;

public class postModel {
    String postId;
    String postTitle;
    String postDescription;
    String postedBy;
    long postedAt;

    int postLike;

    String savedPostId;
    public postModel() {
    }

    public postModel(String posyId, String postTitle, String postDescription, String postedBy, long postedAt) {
        this.postId = posyId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }

    public String getSavedPostId() {
        return savedPostId;
    }

    public void setSavedPostId(String savedPostId) {
        this.savedPostId = savedPostId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getPostLike() {
        return postLike;
    }

}
