package com.internship.socialnetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    private User author;

    private String text;

    private String imagePath;

    private String videoPath;

    @OneToMany
    private List<Comment> comments;

}
