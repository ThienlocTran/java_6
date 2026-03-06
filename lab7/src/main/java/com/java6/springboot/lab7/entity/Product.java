package com.java6.springboot.lab7.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID tự động tăng
    private Integer id;

    private String name;
    private Double price;

    @Temporal(TemporalType.DATE)
    private Date createDate = new Date(); // Ngày nhập tự động lấy ngày hiện tại

    @ManyToOne
    @JoinColumn(name = "categoryId") // Khóa ngoại tham chiếu tới Category
    private Category category;
}