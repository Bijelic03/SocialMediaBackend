package com.internship.socialnetwork.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private char[] password;

    private String name;

    private String surname;

    private String username;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @ManyToMany
    private List<User> friends;


}
