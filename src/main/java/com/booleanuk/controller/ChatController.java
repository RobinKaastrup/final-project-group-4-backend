package com.booleanuk.controller;

import com.booleanuk.model.Chat;
import com.booleanuk.model.Message;
import com.booleanuk.model.User;
import com.booleanuk.repository.ChatRepository;
import com.booleanuk.repository.MessageRepository;
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
    @Autowired
    private MessageRepository messageRepository;

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
    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateChat(@PathVariable int id, @RequestBody Chat chat) {
        Chat chat1 = this.getChat(id);
        if (chat1 == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No chat with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } try {
            chat1.setUpdatedAt(ZonedDateTime.now());
            chat1.setContent(chat.getContent());
            chat1.setUsers(chat.getUsers());
            this.chatRepository.save(chat1);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not update chat");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.set(chat1);
        return new ResponseEntity<>(chatResponse, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteChat(@PathVariable int id) {
        Chat chat1 = this.getChat(id);
        if (chat1 == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No chat with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (!chat1.getMessages().isEmpty()) {
            for (Message message : chat1.getMessages()) {
                this.messageRepository.delete(message);
            }
        }
        this.chatRepository.delete(chat1);
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.set(chat1);
        return ResponseEntity.ok(chatResponse);
    }

    private Chat getChat(int id){
        return this.chatRepository.findById(id)
                .orElse(null);
    }
}
