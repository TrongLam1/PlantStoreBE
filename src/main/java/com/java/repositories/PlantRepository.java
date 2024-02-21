package com.java.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.entities.PlantEntity;
import com.java.entities.PlantStatus;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Long> {

	@Query("SELECT p FROM PlantEntity p WHERE p.valid = :valid")
	Page<PlantEntity> findAllValidPlants(@Param("valid") PlantStatus valid, Pageable pageable);
	
	Page<PlantEntity> findByValidAndNewPriceLessThanEqual(PlantStatus valid, Pageable pageable, Long maxPrice);
	
	Optional<PlantEntity> getOne(long id);
	
	Optional<PlantEntity> findFirstByValidOrderByNewPriceDesc(@Param("valid") PlantStatus valid);
	
	Optional<PlantEntity> findByNameAndValid(@Param("name") String name, @Param("valid") PlantStatus valid);

	List<PlantEntity> findByNameContainingAndValid(@Param("name") String name, @Param("valid") PlantStatus valid);

	@Query("SELECT p FROM PlantEntity p WHERE p.type IN :types AND p.valid = :valid")
	Page<PlantEntity> findByTypeAndValid(@Param("types") List<String> types, @Param("valid") PlantStatus valid, Pageable pageable);
	
	@Query("SELECT p FROM PlantEntity p WHERE p.type IN :types AND p.newPrice >= :minPrice AND p.valid = :valid")
	Page<PlantEntity> findByTypeAndMinPrice(List<String> types, Long minPrice,@Param("valid") PlantStatus valid, Pageable pageable);

	List<PlantEntity> findTop5ByTypeAndValidOrderByTypeDesc(String type, PlantStatus valid);
	
	@Query("SELECT p FROM PlantEntity p WHERE p.newPrice BETWEEN :minPrice AND :maxPrice AND p.valid = :valid")
	Page<PlantEntity> findByPriceRange(@Param("minPrice") Long minPrice,
			@Param("maxPrice") Long maxPrice,@Param("valid") PlantStatus valid, Pageable pageable);

	@Query("SELECT p FROM PlantEntity p WHERE p.type IN :types AND p.newPrice BETWEEN :minPrice AND :maxPrice AND p.valid = :valid")
	Page<PlantEntity> findByTypesAndPriceRange(@Param("types") List<String> types, @Param("minPrice") Long minPrice,
			@Param("maxPrice") Long maxPrice,@Param("valid") PlantStatus valid, Pageable pageable);
}
