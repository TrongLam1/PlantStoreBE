package com.java.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.java.dto.PlantDTO;
import com.java.entities.PlantStatus;
import com.java.exception.PlantException;
import com.java.service.CloudinaryService;
import com.java.service.impl.OrderServiceImpl;
import com.java.service.impl.PlantServiceImpl;
import com.java.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/admin/plant")
public class AdminController {

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private PlantServiceImpl plantServiceImpl;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private OrderServiceImpl orderService;

	@GetMapping("/list")
	public ResponseEntity<?> getAllPlants(
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
		return plantServiceImpl.getAll(pageNo, pageSize, sortBy);
	}

	@GetMapping("/filter/type/{types}")
	public ResponseEntity<?> filterByType(@PathVariable("type") List<String> types,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort) {
		return plantServiceImpl.filterByType(types, pageNo, pageSize, sort);
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "size", defaultValue = "10", required = false) int pageSize) {
		return userService.getUsers(pageNo, pageSize);
	}

	@GetMapping("/getAllOrders")
	public ResponseEntity<?> getAllOrders(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "size", defaultValue = "10", required = false) int pageSize) {
		return orderService.getAllOrders(pageNo, pageSize);
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<?> createNewPlant(@ModelAttribute PlantDTO plantDTO,
			@RequestParam(value = "file") MultipartFile file) throws IOException {
		BufferedImage bi = ImageIO.read(file.getInputStream());
		if (bi == null) {
			return ResponseEntity.ok("Image not valid!");
		}
		Map result = cloudinaryService.upload(file);
		PlantDTO dto = new PlantDTO();
		dto.setName(plantDTO.getName());
		dto.setType(plantDTO.getType());
		dto.setDescription(plantDTO.getDescription());
		dto.setOldPrice(plantDTO.getOldPrice());
		dto.setSale(plantDTO.getSale());
		dto.setNewPrice(plantDTO.getNewPrice());
		dto.setRating(plantDTO.getRating());
		dto.setImageUrl((String) result.get("url"));
		dto.setImageId((String) result.get("public_id"));
		dto.setValid(PlantStatus.Available);
		plantServiceImpl.save(dto);
		return ResponseEntity.ok("Created successfully!");
	}

	@PostMapping("/update/{id}")
	@ResponseBody
	public ResponseEntity<?> updatePlant(@ModelAttribute PlantDTO plantDTO, @PathVariable String id) {
		PlantDTO dto = new PlantDTO();

		long idPlant = Long.parseLong(id);

		PlantDTO old = plantServiceImpl.getOne(idPlant);

		dto.setId(idPlant);
		dto.setName(plantDTO.getName());
		dto.setType(plantDTO.getType());
		dto.setDescription(plantDTO.getDescription());
		dto.setOldPrice(plantDTO.getOldPrice());
		dto.setSale(plantDTO.getSale());
		dto.setNewPrice(plantDTO.getNewPrice());
		dto.setRating(plantDTO.getRating());
		dto.setImageUrl(old.getImageUrl());
		dto.setImageId(old.getImageId());

		plantServiceImpl.update(dto);
		return ResponseEntity.ok("Update successfully!");
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		try {
			return ResponseEntity.ok(plantServiceImpl.delete(id));
		} catch (PlantException e) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Not found");
		}
	}

	@GetMapping("/getTotalPricesByRange")
	public ResponseEntity<?> getTotalPriceByRange(@RequestParam("startDate") String stStartDate,
			@RequestParam("endDate") String stEndDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String start = stStartDate;
		String end = stEndDate;

		Date startDate = dateFormat.parse(start);
		Date endDate = dateFormat.parse(end);

		return orderService.getTotalPriceInDateRange(startDate, endDate);
	}

	@GetMapping("/getTotalPriceInYear/{year}")
	public ResponseEntity<?> getTotalPriceInYear(@PathVariable("year") int year) {
		return orderService.getTotalPriceByMonthInYear(year);
	}

	@GetMapping("/findOrderById/{id}")
	public ResponseEntity<?> getOneOrder(@PathVariable("id") Long orderId) {
		return orderService.findOrderById(orderId);
	}

	@PutMapping("/updateOrderStatus/{id}/{status}")
	public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long orderId, @PathVariable("status") int status) {
		return orderService.updateOrderStatus(orderId, status);
	}
	
	@GetMapping("/count/orderStatus")
	public ResponseEntity<?> countOrderStatus() {
		return ResponseEntity.ok(orderService.countOrdersByStatus());
	}
}
