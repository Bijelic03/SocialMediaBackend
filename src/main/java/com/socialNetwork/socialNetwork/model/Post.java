package com.socialNetwork.socialNetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
