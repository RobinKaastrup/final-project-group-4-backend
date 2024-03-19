package com.booleanuk.controller;

import com.booleanuk.model.User;
import com.booleanuk.model.UserRelation;
import com.booleanuk.repository.UserRelationRepository;
import com.booleanuk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/relations")
public class UserRelationController {
    @Autowired
    UserRelationRepository userRelationRepository;

    @Autowired
    UserRepository userRepository;

    // Endpoint to create a new user relation
    @PostMapping("/add/{userId}/{friendId}")
    public UserRelation createUserRelation(@PathVariable int userId, @PathVariable int friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User with ID " + friendId + " not found"));

        UserRelation userRelation = new UserRelation();
        userRelation.setUser(user);
        userRelation.setFriend(friend);

        return userRelationRepository.save(userRelation);
    }

    // Endpoint to get all user relations
    @GetMapping("/all")
    public List<UserRelation> getAllUserRelations() {
        return userRelationRepository.findAll();
    }

    // Endpoint to get user relations by user ID
    @GetMapping("/{userId}")
    public List<UserRelation> getUserRelationsByUserId(@PathVariable int userId) {
        return userRelationRepository.findByUserId(userId);
    }

    // Endpoint to delete a user relation by its ID
    @DeleteMapping("/delete/{relationId}")
    public ResponseEntity<String> deleteUserRelation(@PathVariable int relationId) {
        Optional<UserRelation> userRelationOptional = userRelationRepository.findById(relationId);
        if (userRelationOptional.isPresent()) {
            userRelationRepository.deleteById(relationId);
            return ResponseEntity.ok("User relation with ID " + relationId + " deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}