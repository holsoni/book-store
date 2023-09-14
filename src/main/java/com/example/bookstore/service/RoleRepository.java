package com.example.bookstore.service;

import com.example.bookstore.model.Role;
import com.example.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByName(RoleName roleName);
}
