package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusSummary {

	private Long pending;
	private Long delivered;
	private Long canceled;
	private Long totalEarn;
}
