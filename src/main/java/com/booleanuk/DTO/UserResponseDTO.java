package com.booleanuk.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private int id;
    private String username;
    private String email;
    private String profileimage;
    private Set<Integer> chatIds;
    private Set<String> roles;

    public UserResponseDTO(int id, String username, String email, String profileimage, Set<Integer> chatIds, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileimage = profileimage;
        this.chatIds = chatIds;
        this.roles = roles;
    }

}