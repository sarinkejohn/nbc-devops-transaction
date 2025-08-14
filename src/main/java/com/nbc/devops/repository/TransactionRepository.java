package com.nbc.devops.repository;

import com.nbc.devops.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  Optional<TransactionEntity> findByReference(String reference);
}
