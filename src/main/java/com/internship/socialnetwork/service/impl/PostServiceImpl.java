package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.PostDto.convertToDto;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String NOT_FOUND_EXCEPTION_MSG = "Post with id %s not found!";

    private final PostRepository postRepository;

    @Override
    public Post getModel(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MSG, id)));
    }

    @Override
    public List<PostDto> getAll() {
        return postRepository.findAll()
                .stream()
                .map(PostDto::convertToDto)
                .toList();
    }

    @Override
    public List<PostDto> getAllByAuthorId(Long authorId) {
        return postRepository.findByAuthor(authorId)
                .stream()
                .map(PostDto::convertToDto)
                .toList();
    }

    @Override
    public PostDto get(Long id) {
        return convertToDto(postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MSG, id))));
    }

    @Override
    public PostDto create(PostDto postDto) {
        return convertToDto(postRepository.save(postDto.convertToModel()));
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = getModel(id);
        post.setText(postDto.getText());
        post.setVideoPath(postDto.getVideoPath());
        post.setImagePath(postDto.getImagePath());
        return convertToDto(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(getModel(id));
    }

}