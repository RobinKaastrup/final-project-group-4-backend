package com.booleanuk.repository;

import com.booleanuk.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChatId(int chatID);

    List<Message> findByChatIdAndUserId(int chatId, int userId);
}
