package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.GachaResult;

public interface GachaResultRepository extends JpaRepository<GachaResult, Long>{
	
	List<GachaResult> findByUserIdOrderByCreatedAtDesc(Long userId);

}
