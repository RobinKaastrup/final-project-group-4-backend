package com.booleanuk.model;

import com.booleanuk.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column(nullable = false)
    private String profileimage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users")
//    @JsonIgnore
    private Set<Chat> chats = new HashSet<>();
    public Set<Integer> getChatIds() {
        return this.chats.stream().map(Chat::getId).collect(Collectors.toSet());
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileimage = generateProfileImageUrl(email);
    }
    public User(String username, String email, String password, String profileimage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileimage = profileimage;
    }

    private String generateProfileImageUrl(String email) {
        return "https://www.gravatar.com/avatar/" + email + "?s=120&d=identicon";
    }
}