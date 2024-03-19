package com.booleanuk.repository;

import com.booleanuk.model.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRelationRepository extends JpaRepository<UserRelation, Integer> {

    // Define a custom query method to find user relations by user ID
    @Query("SELECT ur FROM UserRelation ur WHERE ur.user.id = :userId")
    List<UserRelation> findByUserId(int userId);
}
