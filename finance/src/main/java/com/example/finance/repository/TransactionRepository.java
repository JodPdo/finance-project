package com.example.finance.repository;

import com.example.finance.model.Transaction;
import com.example.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);     // แบบใช้ userId
    List<Transaction> findByUser(User user);         // ✅ แบบใช้ User object
    List<Transaction> findByCategoryId(Long categoryId);
}

