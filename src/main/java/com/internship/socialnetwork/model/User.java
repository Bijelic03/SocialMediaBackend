package com.internship.socialnetwork.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private char[] password;

    @Column(nullable = false)
    private String name;

    private String surname;

    @Column(unique = true, nullable = false, length = 25)
    private String username;

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Friendship> friendships;

    @Enumerated(EnumType.STRING)
    private Role role;

}