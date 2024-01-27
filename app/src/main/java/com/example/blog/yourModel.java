package com.example.blog;

public class yourModel {
    String title;
    String description;
    long postedAt;
    String blogId;

    public yourModel(String heading, String body, long postedAt) {
        this.title = heading;
        this.description = body;
        this.postedAt = postedAt;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public yourModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

}
