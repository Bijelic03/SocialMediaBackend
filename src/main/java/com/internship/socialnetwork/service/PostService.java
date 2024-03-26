package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    List<PostDto> getAllPosts();

    Post getPostModel(Long id);

    PostDto getPost(Long id);

    PostDto createPost(PostDto postDto);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePost(Long id);

}
