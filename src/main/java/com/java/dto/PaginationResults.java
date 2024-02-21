package com.java.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaginationResults<T> {

	private List<T> data;
    private int totalPages;
    private Long totalItems;
}
