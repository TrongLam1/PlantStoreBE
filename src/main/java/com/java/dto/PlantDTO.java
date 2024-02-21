package com.java.dto;

import java.util.Date;

import com.java.entities.PlantStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantDTO {

	private Long id;
	private Date createdDate;
    private String name;
    private String type;
    private String description;
    private Long oldPrice;
    private Long newPrice;
    private double sale;
    private double rating;
    private String imageUrl;
    private String imageId;
    private PlantStatus valid;
}
