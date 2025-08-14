package com.nbc.devops.dto;

import jakarta.validation.constraints.NotBlank;

public class TransactionRequest {
  @NotBlank
  private String service;
  @NotBlank
  private String name;
  @NotBlank
  private String amount;
  @NotBlank
  private String account;
  @NotBlank
  private String reference;

  public TransactionRequest() {}

  public String getService() { return service; }
  public void setService(String service) { this.service = service; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getAmount() { return amount; }
  public void setAmount(String amount) { this.amount = amount; }

  public String getAccount() { return account; }
  public void setAccount(String account) { this.account = account; }

  public String getReference() { return reference; }
  public void setReference(String reference) { this.reference = reference; }
}
