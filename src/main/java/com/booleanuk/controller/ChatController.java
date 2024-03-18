package com.booleanuk.controller;

import com.booleanuk.model.Chat;
import com.booleanuk.repository.ChatRepository;
import com.booleanuk.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chats")
public class ChatController {
    @Autowired
    private ChatRepository chatRepository;

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
    public ResponseEntity<Response<?>> createChat(@RequestBody Chat chat) {
        Chat newChat;
        try {
            newChat = this.chatRepository.save(chat);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not create chat");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.set(newChat);
        return new ResponseEntity<>(chatResponse, HttpStatus.CREATED);
    }
}
