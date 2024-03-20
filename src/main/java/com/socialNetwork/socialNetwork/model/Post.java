package com.socialNetwork.socialNetwork.model;

import java.util.List;

public class Post {

    private Long id;

    private User author;

    private String text;

    private String imagePath;

    private String videoPath;

    private List<Comment> comments;

    public Post(Long id, User author, String text, List<Comment> comments, String imagePath) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.imagePath = imagePath;
        this.comments = comments;
    }

    public Post(Long id, User author, String text, String videoPath, List<Comment> comments) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.videoPath = videoPath;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
