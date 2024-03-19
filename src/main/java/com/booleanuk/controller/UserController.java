package com.booleanuk.controller;

import com.booleanuk.DTO.UserResponseDTO;
import com.booleanuk.model.User;
import com.booleanuk.repository.ChatRepository;
import com.booleanuk.repository.UserRepository;
import com.booleanuk.response.ErrorResponse;
import com.booleanuk.response.Response;
import com.booleanuk.response.UserListResponse;
import com.booleanuk.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = this.getUser(id);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No User with that Id");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getChatIds()
        );

        // Directly return the UserResponseDTO wrapped in a ResponseEntity
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user) {
        User newUser;
        try {
            newUser = this.userRepository.save(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not create User");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        com.booleanuk.response.UserResponse userResponse = new com.booleanuk.response.UserResponse();
        userResponse.set(newUser);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


    private User getUser(int id){
        return this.userRepository.findById(id)
                .orElse(null);
    }
}
