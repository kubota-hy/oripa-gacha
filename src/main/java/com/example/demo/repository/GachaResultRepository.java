package com.example.demo.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.GachaResult;
import com.example.demo.enums.GachaResultType;

public interface GachaResultRepository extends JpaRepository<GachaResult, Long> {
    List<GachaResult> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<GachaResult> findByUserIdAndResultTypeOrderByCreatedAtDesc(Long userId, GachaResultType resultType);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select gr
        from GachaResult gr
        where gr.id = :resultId
    """)
    Optional<GachaResult> findByIdForUpdate(@Param("resultId") Long resultId);
    
    List<GachaResult> findByIdIn(Collection<Long> ids);
}
