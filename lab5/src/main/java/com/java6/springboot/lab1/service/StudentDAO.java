package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDAO extends JpaRepository<Student, String> {
}
