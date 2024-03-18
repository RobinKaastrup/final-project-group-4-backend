package com.booleanuk.controller;

import com.booleanuk.model.Chat;
import com.booleanuk.model.User;
import com.booleanuk.repository.ChatRepository;
import com.booleanuk.repository.UserRepository;
import com.booleanuk.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.booleanuk.DTO.CreateChatRequest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("chats")
public class ChatController {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ChatListResponse> getAllChats() {
        ChatListResponse chatListResponse = new ChatListResponse();
        chatListResponse.set(this.chatRepository.findAll());
        return ResponseEntity.ok(chatListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getChatById(@PathVariable int id) {
        Chat chat = this.chatRepository.findById(id).orElse(null);
        if (chat == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Chat not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.set(chat);
        return ResponseEntity.ok(chatResponse);
    }
    @PostMapping
    public ResponseEntity<Response<?>> createChat(@RequestBody CreateChatRequest chat) {

        List<User> users = userRepository.findAllById(chat.getUserIds());

        if (users.isEmpty() || users.size() != chat.getUserIds().size()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("One or more user IDs are invalid.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Chat newChat = new Chat();
        newChat.setContent(chat.getContent());
        newChat.setCreatedAt(ZonedDateTime.now());
        newChat.setUpdatedAt(ZonedDateTime.now());
        newChat.setUsers(users);

        Chat savedChat = chatRepository.save(newChat);

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.set(savedChat);
        return new ResponseEntity<>(chatResponse, HttpStatus.CREATED);
    }
}
