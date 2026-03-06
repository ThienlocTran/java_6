package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentServiceImpl implements StudentService{

    final StudentDAO dao;

    public StudentServiceImpl(StudentDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Student> findAll() {
        return dao.findAll();
    }
    @Override
    public Student findById(String id) {
        return dao.findById(id).get();
    }
    @Override
    public Student create(Student student) {
        return dao.save(student);
    }
    @Override
    public Student update(Student student) {
        return dao.save(student);
    }
    @Override
    public void deleteById(String id) {
        dao.deleteById(id);
    }
}
