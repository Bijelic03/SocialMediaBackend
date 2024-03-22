package com.internship.socialnetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Post {

    @Id
    private Long id;

    @ManyToOne
    private User author;

    @Column(length = 280)
    private String text;

    private String imagePath;

    private String videoPath;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

}
