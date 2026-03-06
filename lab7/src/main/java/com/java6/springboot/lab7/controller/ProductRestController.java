package com.java6.springboot.lab7.controller;

import com.java6.springboot.lab7.entity.Product;
import com.java6.springboot.lab7.service.ProductDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    final
    ProductDAO dao;

    public ProductRestController(ProductDAO dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Product> getAll() {
        return dao.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return dao.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable("id") Integer id, @RequestBody Product product) {
        return dao.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
    }
}
