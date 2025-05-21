package com.example.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionSummaryDTO {

    @JsonProperty("totalIncome")
    private double income;

    @JsonProperty("totalExpense")
    private double expense;

    @JsonProperty("balance")
    private double balance;

    public TransactionSummaryDTO(double income, double expense) {
        this.income = income;
        this.expense = expense;
        this.balance = income - expense;
    }

    @JsonProperty("totalIncome")
    public double getIncome() {
        return income;
    }

    @JsonProperty("totalExpense")
    public double getExpense() {
        return expense;
    }

    @JsonProperty("balance")
    public double getBalance() {
        return balance;
    }
}




