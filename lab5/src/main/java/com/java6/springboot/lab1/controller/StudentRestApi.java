package com.java6.springboot.lab1.controller;

import com.java6.springboot.lab1.entity.Student;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*") // Cực kỳ quan trọng: Cho phép các trang web HTML gọi API mà không bị chặn lỗi CORS
@RestController
public class StudentRestApi {
    @GetMapping("/students.json")
    public Map<String, Student> findAll() {
        return Database.map;
    }

    @GetMapping("/students/{key}.json")
    public Student findByKey(@PathVariable("key") String key) {
        return Database.map.get(key);
    }

    @PostMapping("/students.json")
    public Map<String, String> create(@RequestBody Student student) {
        String key = Database.getKey();
        Database.map.put(key, student);
        // Bắt chước Firebase: Khi POST thành công thì trả về {"name": "cái_key_vừa_tạo"}
        return Map.of("name", key);
    }

    @PutMapping("/students/{key}.json")
    public Student update(@PathVariable("key") String key, @RequestBody Student student) {
        Database.map.put(key, student);
        return Database.map.get(key);
    }

    @DeleteMapping("/students/{key}.json")
    public void delete(@PathVariable("key") String key) {
        Database.map.remove(key);
    }
}
