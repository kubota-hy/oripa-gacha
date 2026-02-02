package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Gacha;

public interface GachaRepository extends JpaRepository<Gacha, Long>{

}
