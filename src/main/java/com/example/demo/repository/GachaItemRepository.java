package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.GachaItem;

public interface GachaItemRepository extends JpaRepository<GachaItem, Long> {

	//同時に二人が引いて在庫がマイナスになるのを防ぐため
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select gi
        from GachaItem gi
        where gi.gacha.id = :gachaId
          and gi.remainingQty > 0
    """)
    List<GachaItem> findAvailableForUpdate(@Param("gachaId") Long gachaId);
    Optional<GachaItem> findByGachaIdAndCardId(Long gachaId, Long cardId);
}
