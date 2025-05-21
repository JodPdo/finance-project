package com.example.finance.dto;

public class CategorySummaryDTO {
    private String category;
    private double total;

    public CategorySummaryDTO(String category, double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }
}
