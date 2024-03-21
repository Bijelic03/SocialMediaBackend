package com.socialNetwork.socialNetwork.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {

    private Long id;

    private Post post;

    private Comment parentComment;

    private User author;

}
