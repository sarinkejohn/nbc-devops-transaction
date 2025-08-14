package com.nbc.devops.service;

import com.nbc.devops.dto.TransactionRequest;
import com.nbc.devops.model.TransactionEntity;
import com.nbc.devops.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

  private final TransactionRepository repo;

  public TransactionService(TransactionRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public boolean createIfNotExists(TransactionRequest req) {
    var exists = repo.findByReference(req.getReference());
    if (exists.isPresent()) {
      return false;
    }
    TransactionEntity t = new TransactionEntity(
      req.getService(), req.getName(), req.getAmount(), req.getAccount(), req.getReference()
    );
    repo.save(t);
    return true;
  }
}
