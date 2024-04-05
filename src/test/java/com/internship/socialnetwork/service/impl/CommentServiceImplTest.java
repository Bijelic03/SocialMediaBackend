package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.CommentDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.CommentRepository;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final String NOT_FOUND_EXCEPTION_MSG = "Comment with id 1 not found!";

    private final Comment comment = createComment();

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void shouldGetComment_whenGetComment_ifCommentExists() {
        // Given: Mocking new comment
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When: Getting the comment
        CommentDto commentDto = commentService.getComment(comment.getId());

        // Then: Validate commentDto
        assertNotNull(commentDto);
        assertEquals(comment.getText(), commentDto.getText());

        // Verify that commentRepository.findById(...) was called exactly once with comment id
        verify(commentRepository).findById(comment.getId());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetComment_ifCommentDoesNotExist() {
        // Given: Mocking the scenario where the comment does not exist
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When: Getting the comment
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> commentService.getComment(comment.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(NOT_FOUND_EXCEPTION_MSG, exception.getMessage());

        // Verify that commentRepository.findById(...) was called exactly once with id 2
        verify(commentRepository).findById(comment.getId());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void shouldReturnAllPostComments_whenGetAllPostComments_ifCommentsExist() {
        // Given: Mocking list of comments
        when(commentRepository.findAllByPostId(anyLong())).thenReturn(List.of(comment));

        // When: Getting all comments for the post
        List<CommentDto> commentDtos = commentService.getAllPostComments(comment.getPost().getId());

        // Then: Verify that correct number of comments is returned
        assertEquals(1, commentDtos.size());

        // Verify that commentRepository.findAllByPostId() was called exactly once with the post ID
        verify(commentRepository).findAllByPostId(comment.getPost().getId());
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void shouldReturnAllChildComments_whenGetAllChildComments_ifCommentsExist() {
        // Given: Mocking list of comments
        when(commentRepository.findAllByParentCommentId(anyLong())).thenReturn(List.of(comment));

        // When: Getting all comments for the post
        List<CommentDto> commentDtos = commentService.getAllChildComments(2L);

        // Then: Verify that correct number of comments is returned
        assertEquals(1, commentDtos.size());

        // Verify that commentRepository.findAllByParentCommentId() was called exactly once with the parent comment ID
        verify(commentRepository).findAllByParentCommentId(2L);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void shouldCreateComment_whenCreateComment_ifCommentDtoExists() {
        // Given: Mocking new commentDto
        CommentDto commentDto = CommentDto.convertToDto(comment);

        when(postService.getModel(anyLong())).thenReturn(comment.getPost());
        when(userService.getUserModel(anyString())).thenReturn(comment.getAuthor());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment.getParentComment()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When: Creating the comment
        CommentDto createdCommentDto = commentService.createComment(anyLong(), commentDto);

        // Then: Verify that commentDto is created correctly
        assertEquals(commentDto.getParentCommentId(), createdCommentDto.getParentCommentId());
        assertEquals(commentDto.getAuthorUsername(), createdCommentDto.getAuthorUsername());
        assertEquals(commentDto.getText(), createdCommentDto.getText());
        assertEquals(commentDto.getParentCommentId(), createdCommentDto.getParentCommentId());

        // Verify that postService.getModel(...) is called exactly once
        verify(postService).getModel(anyLong());

        // Verify that userService.getUserModel(...) is called exactly twice
        verify(userService).getUserModel(anyString());

        // Verify that commentRepository.findById(...) was called exactly once with parent comment id
        verify(commentRepository).findById(comment.getParentComment().getId());

        // Verify that commentRepository.save(...) was called exactly once with comment
        verify(commentRepository).save(comment);
    }

    @Test
    void shouldDeleteComment_whenDeleteComment_ifCommentExists() {
        // Given: Mocking the scenario where the comment exists
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When: Deleting the comment
        commentService.deleteComment(comment.getId());

        // Then: Verify that the comment is deleted
        verify(commentRepository).delete(comment);

        // Verify that commentRepository.delete(...) was called exactly once with comment
        verify(commentRepository).delete(comment);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteComment_ifCommentDoesNotExist() {
        // Given: Mocking the scenario where the comment does not exist
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When: Deleting the user
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> commentService.deleteComment(comment.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(NOT_FOUND_EXCEPTION_MSG, exception.getMessage());

        // Verify that userRepository.deleteById(...) was not called
        verify(commentRepository, never()).deleteById(any());
    }

    private Comment createComment() {
        return Comment.builder()
                .id(1L)
                .author(User.builder().id(1L).username("username").build())
                .parentComment(Comment.builder().id(2L).build())
                .text("some text")
                .post(Post.builder().id(3L).build())
                .build();
    }

}