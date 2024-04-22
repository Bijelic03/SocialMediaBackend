package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.PostCreateDto;
import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.FileService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.internship.socialnetwork.dto.PostDto.convertToDto;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String NOT_FOUND_EXCEPTION_MSG = "Post with id %s not found!";

    private final PostRepository postRepository;

    private final FileService fileService;

    private final UserService userService;

    @Override
    public Post getModel(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MSG, id)));
    }

    @Override
    public List<PostDto> getPosts(Long authorId) {
        return authorId == null ? getAll() : getAllByAuthorId(authorId);
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
    public PostDto create(PostCreateDto postCreateDto, MultipartFile file) {
        Post post = new Post();
        if(file != null) {
            List<String> list = new ArrayList<>();

            list.add(  fileService.uploadFile(file, "users/" + postCreateDto.getAuthorId() + "/"));
            post.setMediaPaths(list);
        }
        post.setAuthor(userService.getUserModel(postCreateDto.getAuthorId()));
        post.setText(postCreateDto.getText());
        return convertToDto(postRepository.save(post));
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = getModel(id);
        post.setText(postDto.getText());
        return convertToDto(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(getModel(id));
    }

}