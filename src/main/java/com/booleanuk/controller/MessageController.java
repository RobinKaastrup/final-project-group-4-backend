package com.booleanuk.controller;

import com.booleanuk.model.Chat;
import com.booleanuk.model.Message;
import com.booleanuk.model.User;
import com.booleanuk.repository.ChatRepository;
import com.booleanuk.repository.MessageRepository;
import com.booleanuk.repository.UserRepository;
import com.booleanuk.response.ErrorResponse;
import com.booleanuk.response.MessageListResponse;
import com.booleanuk.response.MessageResponse;
import com.booleanuk.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("chats/{chatID}/messages")
    public ResponseEntity<Response<?>> getMessagesForChat(@PathVariable int chatID){
        Chat chat = this.chatRepository.findById(chatID).orElse(null);
        if (chat == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No chat with that ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<Message> messages = this.messageRepository.findByChatId(chatID);
        if (messages.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No messages in this chat");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        MessageListResponse messageListResponse = new MessageListResponse();
        messageListResponse.set(messages);
        return ResponseEntity.ok(messageListResponse);
    }

    @GetMapping("chats/{chatID}/users/{userID}")
    public ResponseEntity<Response<?>> getMessagesForUser(@PathVariable int chatID, @PathVariable int userID){
        Chat chat = this.chatRepository.findById(chatID).orElse(null);
        if (chat == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No chat with that ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        // Fetch messages by chat ID and user ID
        List<Message> messages = this.messageRepository.findByChatIdAndUserId(chatID, userID);
        if (messages.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No messages in this chat for the given user");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        MessageListResponse messageListResponse = new MessageListResponse();
        messageListResponse.set(messages);
        return ResponseEntity.ok(messageListResponse);
    }

    @PostMapping("chats/{chatID}/users/{userID}")
    public ResponseEntity<Response<?>> addMessage(@PathVariable int chatID, @PathVariable int userID, @RequestBody Message message){
        Chat chat = this.chatRepository.findById(chatID).orElse(null);
        if (chat == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No chat with that ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Message newMessage = new Message();
        try {
            newMessage.setContent(message.getContent());
            newMessage.setChat(chat);
            newMessage.setUser(user);
            newMessage.setCreatedAt(ZonedDateTime.now());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could nto create message");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.set(this.messageRepository.save(newMessage));
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
}
