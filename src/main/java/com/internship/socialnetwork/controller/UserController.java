package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.FriendshipDto;
import com.internship.socialnetwork.dto.UserDto;
import com.internship.socialnetwork.service.FriendshipService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final FriendshipService friendshipService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String searchParam) {
        return ResponseEntity.ok(userService.searchUsers(searchParam));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String  username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<FriendshipDto>> getFriends(@PathVariable Long id) {
        return ResponseEntity.ok(friendshipService.getFriends(id));
    }

    @GetMapping("/{id}/friend-requests")
    @PreAuthorize("@authServiceImpl.isAuthorized(#id)")
    public ResponseEntity<List<FriendshipDto>> getFriendRequests(@PathVariable Long id,
                                                                 @RequestParam(required = false) Boolean sent) {
        return ResponseEntity.ok(friendshipService.getFriendRequests(id, sent));
    }

    @PostMapping("/{userId}/friends/{friendId}/requests")
    @PreAuthorize("@authServiceImpl.isAuthorized(#userId)")
    public ResponseEntity<FriendshipDto> sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        return ResponseEntity.status(CREATED).body(friendshipService.createFriendRequest(userId, friendId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @PatchMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#userId)")
    public ResponseEntity<FriendshipDto> acceptFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        return ResponseEntity.ok(friendshipService.acceptFriendRequest(userId, friendId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("@authServiceImpl.isAuthorized(#userId)")
    public ResponseEntity<Void> unfriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}/requests")
    @PreAuthorize("@authServiceImpl.isAuthorized(#userId)")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.noContent().build();
    }

}