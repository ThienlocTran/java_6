package com.java6.springboot.lab7.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.java6.springboot.lab7.entity.User;
public interface UserDAO extends JpaRepository<User, String> {
}
