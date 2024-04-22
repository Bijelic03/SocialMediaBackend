package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    @NotNull
    private UserDto author;

    private String text;

    private List<String> mediaPaths;

    private List<MultipartFile> multipartFiles;


    public static PostDto convertToDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .author(UserDto.convertToDto(post.getAuthor()))
                .text(post.getText())
                .mediaPaths(post.getMediaPaths())
                .build();
    }

    public Post convertToModel(){
        return Post.builder()
                .author(getAuthor().convertToModel())
                .text(getText())
                .mediaPaths(getMediaPaths())
                .build();
    }

}