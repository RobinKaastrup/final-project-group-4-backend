package com.booleanuk.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateChatRequest {

    // Class to handle new chats
    private String content;
    private List<Integer> userIds;

}
