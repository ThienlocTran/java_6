package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, String> {
}
