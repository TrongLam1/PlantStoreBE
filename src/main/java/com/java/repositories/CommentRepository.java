package com.java.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.entities.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findByPlantId(Long plantId);
	
	@Query("SELECT AVG(c.rating) FROM CommentEntity c WHERE c.plant.id = :plantId")
    Double getAverageRatingForPlant(@Param("plantId") Long plantId);
}
