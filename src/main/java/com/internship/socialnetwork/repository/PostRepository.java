package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
