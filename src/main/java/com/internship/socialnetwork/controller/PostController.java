package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.PostCreateDto;
import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll(@RequestParam(required = false) Long authorId) {
        return ResponseEntity.ok(postService.getPosts(authorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @PostMapping
    public PostDto create(@Valid @ModelAttribute PostCreateDto postDto) {
        return postService.create(postDto, postDto.getFile());
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#postDto.author.id)")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.update(id, postDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#postDto.author.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody PostDto postDto) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

}