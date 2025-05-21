package com.example.finance.service;

import com.example.finance.model.Transaction;
import com.example.finance.model.User;
import com.example.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.*;

import com.example.finance.dto.CategorySummaryDTO;
import com.example.finance.dto.MonthlySummaryDTO;
import com.example.finance.dto.TransactionSummaryDTO;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        // ✅ ดึง user ที่ login จาก CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        return transactionRepository.findByUserId(user.getId());
    }

    public Transaction createTransaction(Transaction transaction) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        existing.setAmount(updatedTransaction.getAmount());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setDate(updatedTransaction.getDate());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setUser(updatedTransaction.getUser());

        return transactionRepository.save(existing);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<MonthlySummaryDTO> getMonthlySummary(User user) {
    List<Transaction> transactions = transactionRepository.findByUser(user);

    // สร้าง Map ที่ key = เดือน, value = list ของ transaction
    Map<String, List<Transaction>> grouped = transactions.stream()
        .collect(Collectors.groupingBy(
            t -> t.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")) // เช่น "2025-05"
        ));

    // แปลงเป็น List ของ DTO
    List<MonthlySummaryDTO> summaryList = new ArrayList<>();

    for (String month : grouped.keySet()) {
        double income = 0;
        double expense = 0;

        for (Transaction t : grouped.get(month)) {
    String name = t.getCategory().getName();
    if (name != null && name.equalsIgnoreCase("รายรับ")) {
        income += t.getAmount();
    } else {
        expense += t.getAmount();
    }
}


        summaryList.add(new MonthlySummaryDTO(month, income, expense));
    }

    // เรียงเดือนใหม่ (จากเก่า → ใหม่)
    summaryList.sort(Comparator.comparing(MonthlySummaryDTO::getMonth));

    return summaryList;
}

public List<CategorySummaryDTO> getCategorySummary(User user) {
    List<Transaction> transactions = transactionRepository.findByUser(user);

    Map<String, Double> summaryMap = new HashMap<>();

    for (Transaction t : transactions) {
        if (t.getCategory().getName() != null && !t.getCategory().getName().equalsIgnoreCase("รายรับ")) {
            String name = t.getCategory().getName();
            summaryMap.put(name, summaryMap.getOrDefault(name, 0.0) + t.getAmount());
        }
    }

    List<CategorySummaryDTO> result = new ArrayList<>();
    for (Map.Entry<String, Double> entry : summaryMap.entrySet()) {
        result.add(new CategorySummaryDTO(entry.getKey(), entry.getValue()));
    }

    return result;
}

public TransactionSummaryDTO getTransactionSummary(User user) {
    List<Transaction> transactions = transactionRepository.findByUser(user);

    double income = 0;
    double expense = 0;

    for (Transaction t : transactions) {
        String type = t.getType();
        if (type != null && type.equalsIgnoreCase("income")) {
            income += t.getAmount();
        } else if (type != null && type.equalsIgnoreCase("expense")) {
            expense += t.getAmount();
        }
    }

    return new TransactionSummaryDTO(income, expense);
}

public List<MonthlySummaryDTO> getMonthlySummary(User user) {
    List<Transaction> transactions = transactionRepository.findByUser(user);

    Map<String, Double> incomeMap = new HashMap<>();
    Map<String, Double> expenseMap = new HashMap<>();

    for (Transaction t : transactions) {
        if (t.getDate() == null || t.getType() == null) continue;

        String month = t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue());

        if (t.getType().equalsIgnoreCase("income")) {
            incomeMap.put(month, incomeMap.getOrDefault(month, 0.0) + t.getAmount());
        } else if (t.getType().equalsIgnoreCase("expense")) {
            expenseMap.put(month, expenseMap.getOrDefault(month, 0.0) + t.getAmount());
        }
    }

    Set<String> allMonths = new HashSet<>();
    allMonths.addAll(incomeMap.keySet());
    allMonths.addAll(expenseMap.keySet());

    List<String> sortedMonths = new ArrayList<>(allMonths);
    Collections.sort(sortedMonths);

    List<MonthlySummaryDTO> result = new ArrayList<>();
    for (String month : sortedMonths) {
        double income = incomeMap.getOrDefault(month, 0.0);
        double expense = expenseMap.getOrDefault(month, 0.0);
        result.add(new MonthlySummaryDTO(month, income, expense));
    }

    return result;
}


}

