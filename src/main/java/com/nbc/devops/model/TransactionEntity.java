package com.nbc.devops.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"reference"})
})
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "service_name")
  private String serviceName;

  private String name;
  private String amount;
  private String account;

  @Column(nullable = false, unique = true)
  private String reference;

  private Instant createdAt;

  public TransactionEntity() {
    this.createdAt = Instant.now();
  }

  public TransactionEntity(String serviceName, String name, String amount, String account, String reference) {
    this.serviceName = serviceName;
    this.name = name;
    this.amount = amount;
    this.account = account;
    this.reference = reference;
    this.createdAt = Instant.now();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getServiceName() { return serviceName; }
  public void setServiceName(String serviceName) { this.serviceName = serviceName; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getAmount() { return amount; }
  public void setAmount(String amount) { this.amount = amount; }

  public String getAccount() { return account; }
  public void setAccount(String account) { this.account = account; }

  public String getReference() { return reference; }
  public void setReference(String reference) { this.reference = reference; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
