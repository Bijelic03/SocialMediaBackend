package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDto;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;

import java.util.List;

public interface CommentService {

    CommentDto getComment(Long id);

    List<CommentDto> getAllPostComments(Long postId);

    List<CommentDto> getAllChildComments(Long parentId);

    CommentDto createComment(Long postId, CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto);

    void deleteComment(Long commentId);

}