package com.booleanuk.controller;

import com.booleanuk.DTO.UserResponseDTO;
import com.booleanuk.model.Chat;
import com.booleanuk.model.ERole;
import com.booleanuk.model.Role;
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

import java.util.Set;
import java.util.stream.Collectors;

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

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .map(Enum::name)
                .collect(Collectors.toSet());

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getChatIds(),
                roleNames
        );
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
    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User user1 = this.getUser(id);
        if (user1 == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No customer with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } try {
            user1.setUsername(user.getUsername());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
//            user1.setRoles(user.getRoles());
//            user1.setChats(user.getChats());
            this.userRepository.save(user1);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not update user, please check all fields are correct.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User user1 = this.getUser(id);
        if (user1 == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No User with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (!user1.getChats().isEmpty()){
            for (Chat chat : user1.getChats()){
                chat.getUsers().remove(user1);
                if (chat.getUsers().isEmpty()){
                    this.chatRepository.delete(chat);
                } else {
                    this.chatRepository.save(chat);
                }
            }
        }
        this.userRepository.delete(user1);
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return ResponseEntity.ok(userResponse);
    }

    private User getUser(int id){
        return this.userRepository.findById(id)
                .orElse(null);
    }
}
