package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotNull
    private User author;

    private String text;

    private String imagePath;

    private String videoPath;

    public static PostDto convertToDto(Post post){
        return PostDto.builder()
                .author(post.getAuthor())
                .text(post.getText())
                .imagePath(post.getImagePath())
                .videoPath(post.getVideoPath())
                .build();
    }

    public Post convertToModel(){
        return Post.builder()
                .author(getAuthor())
                .text(getText())
                .imagePath(getImagePath())
                .videoPath(getVideoPath())
                .build();
    }

}