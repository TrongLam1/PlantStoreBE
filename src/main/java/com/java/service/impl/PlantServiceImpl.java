package com.java.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.parser.Part.Type;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.converter.PlantConverter;
import com.java.dto.PaginationResults;
import com.java.dto.PlantDTO;
import com.java.entities.PlantEntity;
import com.java.entities.PlantStatus;
import com.java.repositories.PlantRepository;
import com.java.service.IPlantService;

@Service
@Transactional
public class PlantServiceImpl implements IPlantService {

	@Autowired
	private PlantRepository plantRepository;

	@Override
	public ResponseEntity<?> save(PlantDTO plantDTO) {
		try {
			PlantEntity entity = new PlantEntity();
			entity = PlantConverter.toEntity(plantDTO);
			plantRepository.save(entity);

			return ResponseEntity.ok("Thêm sản phẩm mới thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thêm sản phẩm mới thất bại.");
		}
	}
	
	@Override
	public ResponseEntity<?> update(PlantDTO plantDTO) {
		try {
			PlantEntity entity = new PlantEntity();
			PlantEntity oldPlant = plantRepository.findById(plantDTO.getId()).get();
			entity = PlantConverter.toEntity(plantDTO, oldPlant);
			entity.setCreatedDate(new Date());
			entity.setValid(PlantStatus.Available);
			plantRepository.save(entity);
			
			return ResponseEntity.ok("Cập nhật sản phẩm thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cập nhật sản phẩm không thành công.");
		}
	}

	@Override
	public ResponseEntity<?> delete(long id) {
		try {
			PlantEntity plant = plantRepository.findById(id).get();
			plant.setValid(PlantStatus.Deleted);
			plantRepository.save(plant);
			return ResponseEntity.ok("Xóa sản phẩm thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa sản phẩm thất bại.");
		}
	}

	@Override
	public ResponseEntity<?> findByName(String name) {
		List<PlantEntity> entities = plantRepository.findByNameContainingAndValid(name, PlantStatus.Available);
		
		if (entities.size() == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm.");
		} else {
			List<PlantDTO> results = new ArrayList<>();
			for (PlantEntity item : entities) {
				PlantDTO dto = PlantConverter.toDTO(item);
				results.add(dto);
			}

			return ResponseEntity.ok(results);
		}
	}

	@Override
	public PlantDTO getOne(long id) {
		Optional<PlantEntity> optionalEntity = plantRepository.findById(id);
		PlantEntity entity = optionalEntity.get();
		PlantDTO dto = PlantConverter.toDTO(entity);
		return dto;
	}

	@Override
	public ResponseEntity<?> filterByType(List<String> types, int pageNo, int pageSize, int sort) {
		Pageable pageable = null;
		if (sort == 0) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "newPrice"));
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "newPrice"));
		}
		Page<PlantEntity> list = plantRepository.findByTypeAndValid(types, PlantStatus.Available, pageable);
		List<PlantDTO> results = new ArrayList<>();
		for (PlantEntity item : list) {
			PlantDTO dto = PlantConverter.toDTO(item);
			results.add(dto);
		}
		
		PaginationResults<PlantDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(list.getTotalPages());
		
		return ResponseEntity.ok(paginationResult);
	}

	@Override
	public ResponseEntity<?> getAll(int pageNo, int pageSize, String sortBy) {
		long totalItems = plantRepository.count();
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		if (pageNo < 1 || pageNo > totalPages) {
			throw new IllegalArgumentException("Trang không hợp lệ.");
		}

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
		Page<PlantEntity> list = plantRepository.findAllValidPlants(PlantStatus.Available, pageable);
		List<PlantDTO> results = new ArrayList<>();
		for (PlantEntity item : list) {
			PlantDTO dto = PlantConverter.toDTO(item);
			results.add(dto);
		}

		PaginationResults<PlantDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(totalPages);
		paginationResult.setTotalItems(totalItems);

		return ResponseEntity.ok(paginationResult);
	}

	@Override
	public ResponseEntity<?> getTop5PlantByType(String type) {
		List<PlantEntity> list = plantRepository.findTop5ByTypeAndValidOrderByTypeDesc(type, PlantStatus.Available);
		List<PlantDTO> results = new ArrayList<>();
		for (PlantEntity item : list) {
			PlantDTO dto = PlantConverter.toDTO(item);
			results.add(dto);
		}

		return ResponseEntity.ok(results);
	}

	@Override
	public ResponseEntity<?> findByTypeAndPriceRange(List<String> types, Long minPrice, Long maxPrice,
			int pageNo, int pageSize, int sort, String sortBy) {
		Pageable pageable = null;
		if (sort == 0) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
		}
		
		Page<PlantEntity> list;
		
		if (types.isEmpty() && maxPrice == 0) {
			list = plantRepository.findAllValidPlants(PlantStatus.Available, pageable);
			System.out.println("1");
		} else if (!types.isEmpty() && minPrice > 0 && maxPrice > 0) {
			list = plantRepository.findByTypesAndPriceRange(types, minPrice, maxPrice, PlantStatus.Available, pageable);
			System.out.println("3");
		} else if (minPrice == 0 && !types.isEmpty()) {
			list = plantRepository.findByTypeAndValid(types, PlantStatus.Available, pageable);
			System.out.println("4");
		} else if (types == null || types.isEmpty() && minPrice == 0) {
			list = plantRepository.findByPriceRange(minPrice, maxPrice, PlantStatus.Available, pageable);
			System.out.println("5");
		} else {
			list = plantRepository.findByTypeAndMinPrice(types, minPrice, PlantStatus.Available, pageable);
			System.out.println("6");
		}
		
		if (list == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm.");
	    }

		List<PlantDTO> results = new ArrayList<>();
		for (PlantEntity item : list) {
			PlantDTO dto = PlantConverter.toDTO(item);
			results.add(dto);
		}
		
		PaginationResults<PlantDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(list.getTotalPages());

		return ResponseEntity.ok(paginationResult);
	}
}
