package com.java6.springboot.lab7.controller;

import com.java6.springboot.lab7.entity.User;
import com.java6.springboot.lab7.service.UserDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserRestController {


  final   UserDAO dao;

    public UserRestController(UserDAO dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<User> getAll() {
        return dao.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return dao.save(user);
    }

    @PutMapping("/{username}")
    public User update(@PathVariable("username") String username, @RequestBody User user) {
        return dao.save(user);
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable("username") String username) {
        dao.deleteById(username);
    }
}