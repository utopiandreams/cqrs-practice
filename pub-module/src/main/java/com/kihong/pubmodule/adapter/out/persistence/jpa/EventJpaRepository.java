package com.kihong.pubmodule.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventJpaRepository extends JpaRepository<EventRecordEntity, Long> {

    @Query("select e from EventRecordEntity e where e.transactionId = :transactionId and e.isPublished = 'N'")
    Optional<EventRecordEntity> findByTransactionId(@Param("transactionId") String transactionId);

    @Query("select e from EventRecordEntity e ORDER BY e.createdAt desc limit 1")
    Optional<EventRecordEntity> findLatest();

}
