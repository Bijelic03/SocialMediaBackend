package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.CommentDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.repository.CommentRepository;
import com.internship.socialnetwork.service.CommentService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.CommentDto.convertToDto;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String COMMENT_NOT_FOUND_MSG = "Comment with id %s not found!";

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final UserService userService;

    @Override
    public CommentDto getComment(Long commentId) {
        return CommentDto.convertToDto(getCommentModel(commentId));
    }

    @Override
    public List<CommentDto> getAllPostComments(Long postId) {
        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(CommentDto::convertToDto)
                .toList();
    }

    @Override
    public List<CommentDto> getAllChildComments(Long parentId) {
        return commentRepository.findAllByParentCommentId(parentId)
                .stream()
                .map(CommentDto::convertToDto)
                .toList();
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = commentDto.convertToModel();
        Long parentCommentId = commentDto.getParentCommentId();

        if (parentCommentId != null) {
            comment.setParentComment(getCommentModel(parentCommentId));
        }

        comment.setPost(postService.getModel(postId));
        comment.setAuthor(userService.getUserModel(commentDto.getAuthorUsername()));

        return convertToDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = getCommentModel(commentDto.getId());
        comment.setText(commentDto.getText());
        return convertToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.delete(getCommentModel(commentId));
    }

    private Comment getCommentModel(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MSG, id)));
    }

}