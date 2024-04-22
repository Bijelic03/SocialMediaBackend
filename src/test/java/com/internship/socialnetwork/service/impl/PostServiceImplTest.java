package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.PostDto;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    private static final String NOT_FOUND_EXCEPTION_MSG = "Post with id 1 not found!";

    private final User author = createUser();

    private final Post post = createPost(1L);

    private final Post otherPost = createPost(2L);

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void shouldReturnPost_whenGetPost_ifPostExists() {
        // Given: Mocking new post
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When: Getting the post
        PostDto postDto = postService.get(post.getId());

        // Then: Validate postDto
        assertNotNull(postDto);
        assertEquals(post.getAuthor().getId(), postDto.getAuthor().getId());

        // Verify that postRepository.findById(...) was called exactly once with postId1
        verify(postRepository).findById(post.getId());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetPost_ifPostDoesNotExist() {
        // Given: Mocking the scenario where the post does not exist
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        // When: Getting post
        NotFoundException exception = assertThrows(NotFoundException.class, () -> postService.get(post.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(NOT_FOUND_EXCEPTION_MSG, exception.getMessage());

        // Verify that postRepository.findById(...) was called exactly once with userId2
        verify(postRepository).findById(post.getId());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void shouldReturnAllPosts_whenGetAll_ifPostsExist() {
        // Given: Mocking list of posts
        when(postRepository.findAll()).thenReturn(List.of(
                post,
                otherPost
        ));

        // When: Getting all posts
        List<PostDto> postDtos = postService.getAll();

        // Then: Verify that correct number of posts is returned
        assertEquals(2, postDtos.size());

        // Verify that postRepository.findAll() was called exactly once
        verify(postRepository).findAll();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void shouldReturnAllPostsBySameAuthor_whenGetAllByAuthorId_ifPostsExist() {
        // Given: Mocking list of posts
        when(postRepository.findByAuthor(post.getAuthor().getId())).thenReturn(List.of(
                post,
                otherPost
        ));

        // When: Getting all posts with same author
        List<PostDto> postDtos = postService.getAllByAuthorId(post.getAuthor().getId());

        // Then: Verify that correct number of posts is returned
        assertEquals(2, postDtos.size());

        // Verify that postRepository.findByAuthor() was called exactly once
        verify(postRepository).findByAuthor(post.getAuthor().getId());
        verifyNoMoreInteractions(postRepository);
    }

//    @Test
//    void shouldCreatePost_whenCreate_ifPostDtoExists() {
//        // Given:
//        PostDto postDto = PostDto.convertToDto(post);
//
//        when(postRepository.save(any(Post.class))).thenReturn(post);
//
//        // When: Creating the post
//        PostDto createdPostDto = postService.create(postDto);
//
//        // Then: Verify that postDto is created correctly
//        assertEquals(postDto.getAuthor(), createdPostDto.getAuthor());
//        assertEquals(postDto.getText(), createdPostDto.getText());
//        assertEquals(postDto.getImagePath(), createdPostDto.getImagePath());
//
//        // Verify that postRepository.save(...) was called exactly once with any(Post.class) argument
//        verify(postRepository).save(any(Post.class));
//
//        // Verify that there are no more interactions with postRepository
//        verifyNoMoreInteractions(postRepository);
//    }

    @Test
    void shouldDeletePost_whenDelete_ifPostExists() {
        // Given: Mocking the scenario where the post exists
        Post post = Post.builder().build();
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When: Deleting the post
        postService.delete(post.getId());

        // Then: Verify that the post is deleted
        assertEquals(0, postService.getAll().size());

        // Verify that postRepository.delete(...) was called exactly once with the post ID
        verify(postRepository).delete(post);

        // Verify that postRepository.findById(...) was called exactly once with the post ID
        verify(postRepository).findById(post.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifPostDoesNotExist() {
        // Given: Mocking the scenario where the post does not exist
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        // When: Deleting the post
        NotFoundException exception = assertThrows(NotFoundException.class, () -> postService.delete(post.getId()));

        // Then: Verify that NotFoundException is thrown with appropriate message
        assertEquals(NOT_FOUND_EXCEPTION_MSG, exception.getMessage());

        // Verify that postRepository.deleteById(...) was not called
        verify(postRepository, never()).deleteById(any());

        // Verify that postRepository.findById(...) was called exactly once with the post ID
        verify(postRepository).findById(post.getId());
    }

    private User createUser() {
         return User.builder()
                .id(1L)
                .username("john")
                .build();
    }

    private Post createPost(Long id) {
        return Post.builder()
                .id(id)
                .text("text")
                .author(author)
//                .imagePath("path")
                .build();
    }

}