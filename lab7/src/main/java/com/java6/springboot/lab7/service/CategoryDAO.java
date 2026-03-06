package com.java6.springboot.lab7.service;

import com.java6.springboot.lab7.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, String> {
}
