package com.socialNetwork.socialNetwork.model;

public class Comment {

    private Long id;

    private Post post;

    private Comment parentComment;

    private User author;

    public Comment(Long id, Post post, Comment parentComment, User author) {
        this.id = id;
        this.post = post;
        this.parentComment = parentComment;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
