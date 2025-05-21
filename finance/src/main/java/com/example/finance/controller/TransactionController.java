package com.example.finance.controller;

import com.example.finance.model.Transaction;
import com.example.finance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.finance.dto.CategorySummaryDTO;
import com.example.finance.dto.MonthlySummaryDTO;
import com.example.finance.dto.TransactionSummaryDTO;
import com.example.finance.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.finance.model.User;


import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping
public Transaction createTransaction(
        @RequestBody Transaction transaction,
        @AuthenticationPrincipal CustomUserDetails userDetails
) {
    // ✅ ผูกผู้ใช้งานที่ login กับ transaction
    transaction.setUser(userDetails.getUser());

    return transactionService.createTransaction(transaction);
}


    @PutMapping("/{id}")
public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
    return transactionService.updateTransaction(id, transaction);
    }

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
    transactionService.deleteTransaction(id);
    return ResponseEntity.noContent().build();
    }

@GetMapping("/summary")
public List<MonthlySummaryDTO> getMonthlySummary(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return transactionService.getMonthlySummary(userDetails.getUser());
}

@GetMapping("/summary-by-category")
public List<CategorySummaryDTO> getCategorySummary() {
    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userDetails.getUser();
    return transactionService.getCategorySummary(user);
}

@GetMapping("/summary/total")
public TransactionSummaryDTO getSummary() {
    CustomUserDetails userDetails = (CustomUserDetails)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return transactionService.getTransactionSummary(userDetails.getUser());
}

@GetMapping("/summary/monthly")
public List<MonthlySummaryDTO> getMonthlySummary(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return transactionService.getMonthlySummary(userDetails.getUser());
}


}

