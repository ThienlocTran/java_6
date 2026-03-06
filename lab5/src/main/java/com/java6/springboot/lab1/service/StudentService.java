package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();
    Student findById(String id);
    Student create(Student student);
    Student update(Student student);
    void deleteById(String id);

}
