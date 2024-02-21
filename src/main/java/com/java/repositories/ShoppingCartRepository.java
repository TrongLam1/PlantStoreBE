package com.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.entities.ShoppingCartEntity;
import com.java.entities.UserEntity;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {
	
	ShoppingCartEntity findByUser(UserEntity user);
}
