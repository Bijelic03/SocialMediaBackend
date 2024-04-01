package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getOne(@RequestParam Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    //ToDo: Create getAllPostsById in the user controller

    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody PostDto postDto) {
        return ResponseEntity.status(CREATED).body(postService.create(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@RequestParam Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.update(id, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

}