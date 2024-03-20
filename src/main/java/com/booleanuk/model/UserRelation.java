package com.booleanuk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_relations")
public class UserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    public UserRelation(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}