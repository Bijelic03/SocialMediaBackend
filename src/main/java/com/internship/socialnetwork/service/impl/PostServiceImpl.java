package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public Post getPostModel(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map(PostDto::convertToDto).collect(Collectors.toList());
    }

    public PostDto getPost(Long id) {
        return PostDto.convertToDto(postRepository.findById(id).orElseThrow(() -> new NotFoundException()));
    }

    public PostDto createPost(PostDto postDto) {
        Post post = postDto.convertToModel();
        return PostDto.convertToDto(postRepository.save(post));
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = getPostModel(id);
        post.setText(postDto.getText());
        post.setVideoPath(postDto.getVideoPath());
        post.setImagePath(postDto.getImagePath());
        return PostDto.convertToDto(postRepository.save(post));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}
