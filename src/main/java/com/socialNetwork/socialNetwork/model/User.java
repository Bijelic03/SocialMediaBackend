package com.socialNetwork.socialNetwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
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
