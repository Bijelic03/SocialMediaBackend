package com.internship.socialnetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
@Data
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    private Long id;

    @OneToOne
    private Post post;

    @ManyToOne
    private Comment parentComment;

    private User author;

}