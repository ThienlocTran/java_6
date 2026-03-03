package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, String> {
}
