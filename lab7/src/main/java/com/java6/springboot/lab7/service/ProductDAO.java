package com.java6.springboot.lab7.service;

import com.java6.springboot.lab7.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, Integer> {
}
