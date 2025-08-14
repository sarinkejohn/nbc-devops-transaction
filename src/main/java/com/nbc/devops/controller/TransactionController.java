package com.nbc.devops.controller;

import com.nbc.devops.dto.ApiResponse;
import com.nbc.devops.dto.TransactionRequest;
import com.nbc.devops.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nbc-bank/devops/v1")
public class TransactionController {

  private final TransactionService service;

  public TransactionController(TransactionService service) {
    this.service = service;
  }

  @PostMapping("/transactions")
  public ResponseEntity<ApiResponse> createTransaction(@Valid @RequestBody TransactionRequest req) {
    boolean created = service.createIfNotExists(req);
    if (created) {
      return ResponseEntity.ok(new ApiResponse("600", "Transaction has been processed"));
    } else {
      return ResponseEntity.status(409).body(new ApiResponse("601", "Duplicate reference received, try with one"));
    }
  }
}
