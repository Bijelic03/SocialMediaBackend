package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByParentCommentId(Long parentCommentId);

}