package com.java.entities;

public enum OrderStatus {

	PENDING("Đang xử lí"),
    DELIVERED("Đã giao"),
    CANCEL("Đã hủy");

    private final String displayValue;

    OrderStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
