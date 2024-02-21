package com.java.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.java.dto.PlantDTO;

public interface IPlantService {

	ResponseEntity<?> save(PlantDTO plantDTO);
	
	ResponseEntity<?> update(PlantDTO plantDTO);

	PlantDTO getOne(long id);

	ResponseEntity<?> getAll(int pageNo, int pageSize, String sortBy);

	ResponseEntity<?> delete(long id);

	ResponseEntity<?> findByName(String name);

	ResponseEntity<?> filterByType(List<String> types, int pageNo, int pageSize, int sort);

	ResponseEntity<?> getTop5PlantByType(String type);

	ResponseEntity<?> findByTypeAndPriceRange(List<String> types, Long minPrice, Long maxPrice, int pageNo,
			int pageSize,int sort, String sortBy);
}
