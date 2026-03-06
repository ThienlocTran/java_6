package com.java6.springboot.lab7.controller;

import com.java6.springboot.lab7.entity.Category;
import com.java6.springboot.lab7.service.CategoryDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

   final CategoryDAO dao;
   public CategoryRestController(CategoryDAO dao) {
       this.dao = dao;
   }

    @GetMapping
    public List<Category> getAll() {
        return dao.findAll();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return dao.save(category);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable("id") String id, @RequestBody Category category) {
        return dao.save(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}
