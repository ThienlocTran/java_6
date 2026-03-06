package com.java6.springboot.lab1.controller;

import com.java6.springboot.lab1.entity.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Database {
    public static Map<String, Student> map = new HashMap<>();

    // Lấy key ngẫu nhiên giống hệt kiểu của Firebase (-Oxyz...)
    public static String getKey() {
        return Integer.toHexString(UUID.randomUUID().toString().hashCode());
    }

    // Nạp sẵn một vài dữ liệu mẫu khi ứng dụng vừa chạy
    static {
        // Lưu ý: Thứ tự truyền vào là (id, name, mark, gender) cho khớp với class Student của bạn
        map.put(getKey(), new Student("SV01", "Lý Thái Tổ", 9.5, true));
        map.put(getKey(), new Student("SV02", "Lê Trọng Tấn", 4.5, true));
        map.put(getKey(), new Student("SV03", "Nguyễn Thị Minh Khai", 8.5, false));
        map.put(getKey(), new Student("SV04", "Đoàn Trung Trực", 6.0, true));
    }
}
