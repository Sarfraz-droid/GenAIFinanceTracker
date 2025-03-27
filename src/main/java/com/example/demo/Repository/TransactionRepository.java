package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    
    @Query("SELECT t FROM TransactionEntity t WHERE t.date = :date")
    List<TransactionEntity> findByDate(@Param("date") String date);
}
