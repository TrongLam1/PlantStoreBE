package com.java.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.entities.Role;
import com.java.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByPhone(String phone);
	UserEntity findByRole(Role role);
}
