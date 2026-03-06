package com.java6.springboot.lab1.controller;

import com.java6.springboot.lab1.entity.Student;
import com.java6.springboot.lab1.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController

public class StudentRestApiBai4 {

    final StudentService studentService;

    public StudentRestApiBai4(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("students") //=> [student1, student2,...]
    public List<Student> findAll(){
        return studentService.findAll();
    }
    @GetMapping("students/{id}") //=> student
    public Student findById(@PathVariable("id") String id){
        return studentService.findById(id);
    }
    @PostMapping("students") //=> student
    public Student create(@RequestBody Student student){
        return studentService.create(student);
    }
    @PutMapping("students/{id}") //=> student
    public Student update(@PathVariable("id") String id, @RequestBody
    Student student){
        return studentService.update(student);
    }
    @DeleteMapping("students/{id}") //=> nothing
    public void delete(@PathVariable("id") String id){
        studentService.deleteById(id);
    }

}
