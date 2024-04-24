package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;

    @NotBlank(message = "Comment text is empty!")
    @Size(max = 100, message = "Comment text cannot exceed 100 characters.")
    private String text;

    @NotBlank
    private String authorUsername;

    private Long parentCommentId;

    private Long postId;

    private Long postAuthorId;

    public static CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .postAuthorId(comment.getPost().getAuthor().getId())
                .text(comment.getText())
                .authorUsername(comment.getAuthor().getUsername())
                .parentCommentId(Optional.ofNullable(comment.getParentComment())
                        .map(Comment::getId)
                        .orElse(null))
                .build();
    }

    public Comment convertToModel() {
        return Comment.builder()
                .id(getId())
                .text(getText())
                .build();
    }

}