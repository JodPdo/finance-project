package com.example.finance.repository;

import com.example.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // อันเดิม (ยังไม่ต้องลบนะ)
    User findByEmail(String email);        // เพิ่มใหม่สำหรับ login ด้วย email
}

