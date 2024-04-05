package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    List<PostDto> getAll();

    List<PostDto> getPosts(Long authorId);

    List<PostDto> getAllByAuthorId(Long authorId);

    Post getModel(Long id);

    PostDto get(Long id);

    PostDto create(PostDto postDto);

    PostDto update(Long id, PostDto postDto);

    void delete(Long id);

}