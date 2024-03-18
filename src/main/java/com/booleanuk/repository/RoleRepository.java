package com.booleanuk.repository;

import com.booleanuk.model.ERole;
import com.booleanuk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}