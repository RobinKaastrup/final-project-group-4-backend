package com.booleanuk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;


// Dummy class for messages. Will change later
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIncludeProperties(value = {"chat_id"})
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"user_id"})
    private User user;

}
