package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.CommentDto;
import com.internship.socialnetwork.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllPostComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllPostComments(postId));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<CommentDto>> getAllCommentReplies(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getAllChildComments(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> create(@PathVariable Long postId, @Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(CREATED).body(commentService.createComment(postId, commentDto));
    }

    @PutMapping
    public ResponseEntity<CommentDto> update(@Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}