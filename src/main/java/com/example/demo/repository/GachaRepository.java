package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Gacha;

public interface GachaRepository extends JpaRepository<Gacha, Long>{

	@Modifying
    @Query("UPDATE Gacha g SET g.remainingStock = g.remainingStock + :addQty WHERE g.id = :gachaId")
    void incrementRemainingStock(@Param("gachaId") Long gachaId, @Param("addQty") int addQty);

}
