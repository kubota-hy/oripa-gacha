package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Shipping;
import com.example.demo.enums.ShippingStatus;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
	List<Shipping> findByUserIdOrderByCreatedAtDesc(Long userId);

	@Query("""
			select s from Shipping s
			join GachaResult r on r.id = s.gachaResultId
			join fetch r.card
			where s.userId = :userId
			order by s.createdAt desc
			""")
	List<Shipping> findHistoryWithCard(@Param("userId") Long userId);

	// 管理者：発送待ち一覧
	List<Shipping> findByStatusOrderByCreatedAtDesc(ShippingStatus status);

	// 発送依頼の重複防止チェック（gachaResultIdはユニーク前提）
	Optional<Shipping> findByGachaResultId(Long gachaResultId);

	boolean existsByGachaResultId(Long gachaResultId);

}
