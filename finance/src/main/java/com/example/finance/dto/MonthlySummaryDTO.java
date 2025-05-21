package com.example.finance.dto;

public class MonthlySummaryDTO {
    private String month;     // ตัวอย่าง: "2025-05"
    private Double totalIncome;
    private Double totalExpense;

    public MonthlySummaryDTO() {}

    public MonthlySummaryDTO(String month, Double totalIncome, Double totalExpense) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
