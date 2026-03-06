package com.java6.springboot.lab1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Userroles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "username")
    User user;

    @ManyToOne
    @JoinColumn(name = "roleid")
    Role role;
}