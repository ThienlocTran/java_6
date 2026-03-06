package com.java6.springboot.lab7.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    private String username;
    private String password;
    private String fullname;
    private Boolean enabled = true; // Mặc định là Hoạt động
    private String role = "USER"; // Mặc định là USER
}