package com.internship.socialnetwork.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    private Long id;

    @Column(length = 100)
    private String text;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment parentComment;

    @ManyToOne
    private User author;

}