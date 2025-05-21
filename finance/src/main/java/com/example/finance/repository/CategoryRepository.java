package com.example.finance.repository;

import com.example.finance.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Optional: เพิ่ม method ค้นหาด้วยชื่อ category
    Category findByName(String name);
}
