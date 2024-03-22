package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentDto {

    @NonNull
    private String text;

    @NonNull
    private User author;

    public static CommentDto convertToDto(Comment comment){
        return CommentDto.builder()
                .author(comment.getAuthor())
                .text(comment.getText())
                .build();
    }

    public Comment convertToModel(){
        return Comment.builder()
                .author(getAuthor())
                .text(getText())
                .build();
    }

}
