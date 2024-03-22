package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
