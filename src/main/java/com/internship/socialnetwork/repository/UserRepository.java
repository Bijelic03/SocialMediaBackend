package com.internship.socialnetwork.repository;

import com.internship.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.username = :username OR user.email = :email")
    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(String username, String name);

    Optional<User> findByUsername(String username);

}