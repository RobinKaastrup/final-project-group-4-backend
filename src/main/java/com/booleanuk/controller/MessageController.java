package com.booleanuk.controller;

import com.booleanuk.model.Message;
import com.booleanuk.repository.MessageRepository;
import com.booleanuk.response.ErrorResponse;
import com.booleanuk.response.MessageListResponse;
import com.booleanuk.response.MessageResponse;
import com.booleanuk.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public ResponseEntity<MessageListResponse> getAllMessages(){
        MessageListResponse messageListResponse = new MessageListResponse();
        messageListResponse.set(this.messageRepository.findAll());
        return ResponseEntity.ok(messageListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getMessageById(@PathVariable int id) {
        Message message = this.messageRepository.findById(id).orElse(null);
        if (message == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Message not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.set(message);
        return ResponseEntity.ok(messageResponse);
    }
}
