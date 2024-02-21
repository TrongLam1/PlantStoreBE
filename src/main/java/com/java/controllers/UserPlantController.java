package com.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.service.impl.PlantServiceImpl;

@RestController
@RequestMapping("/plant")
public class UserPlantController {

	@Autowired
	private PlantServiceImpl plantServiceImpl;
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findProductById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(plantServiceImpl.getOne(id));
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllPlants(
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
		return plantServiceImpl.getAll(pageNo, pageSize, sortBy);
	}

	@GetMapping("/filter/type/{types}")
	public ResponseEntity<?> filterByType(@PathVariable("types") List<String> types,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort) {
		return plantServiceImpl.filterByType(types, pageNo, pageSize, sort);
	}

	@GetMapping("/top/{type}")
	public ResponseEntity<?> getTop5(@PathVariable("type") String type) {
		return plantServiceImpl.getTop5PlantByType(type);
	}

	@GetMapping("/find/{name}")
	public ResponseEntity<?> findByName(@PathVariable("name") String name) {
		return plantServiceImpl.findByName(name);
	}

	@GetMapping("/find/")
	public ResponseEntity<?> findByTypeAndPriceRange(
			@RequestParam(value = "types", required = false) List<String> types,
			@RequestParam(value = "minPrice", defaultValue = "0", required = false) Long minPrice,
			@RequestParam(value = "maxPrice", required = false) Long maxPrice,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort,
			@RequestParam(value = "sortBy", defaultValue = "newPrice", required = false) String sortBy) {
		return plantServiceImpl.findByTypeAndPriceRange(types, minPrice, maxPrice, pageNo, pageSize, sort, sortBy);
	}
}
