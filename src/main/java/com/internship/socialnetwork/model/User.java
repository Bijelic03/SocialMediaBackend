package com.internship.socialnetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    private Long id;

    private String email;

    private String password;

    private String name;

    private String surname;

    private String username;

    @OneToMany
    private List<Post> posts;

    @ManyToMany
    private List<User> friends;


}
