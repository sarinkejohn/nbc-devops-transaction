
# NBC DevOps Transaction - Rollback Plan

## Purpose
This document outlines the steps to safely revert the NBC DevOps Transaction service to a previous stable state in case of deployment failure or critical issues.

---

## Scope
- Spring Boot service
- MySQL database
- Docker Compose deployment
- Kubernetes/ArgoCD deployment (if applicable)

---

## Rollback Steps

### 1. Identify the Issue
- Monitor application logs (`docker logs nbc_devops_service`) or Kubernetes logs.
- Confirm the issue cannot be fixed immediately in the current deployment.

### 2. Stop Current Service
**Docker Compose:**
```
docker-compose down
```
**Kubernetes/ArgoCD:**
- Revert the ArgoCD application to the last known healthy commit using the ArgoCD UI:
  - Navigate to the application
  - Select "History and Rollback"
  - Rollback to the previous stable revision

### 3. Restore Previous Version

**Docker Compose:**
- Checkout the previous stable version in Git:
```
git checkout <previous-stable-commit>
```
- Rebuild and restart:
```
docker-compose up --build -d
```

**Kubernetes:**
- ArgoCD rollback ensures previous manifests and container images are redeployed automatically.

### 4. Verify System Health
- Confirm the application is running at `http://localhost:8080` (Docker) or via service endpoint (Kubernetes).
- Run a test transaction to verify database connectivity and service functionality.

### 5. Communicate Status
- Notify stakeholders that the rollback was performed.
- Document the cause and resolution plan for future reference.

---

## Notes
- Always maintain backups of the MySQL database before deploying changes.
- Ensure all rollback procedures are tested in a staging environment before applying in production.