package com.example.orchestration.repository;

import com.example.orchestration.entity.SagaState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SagaStateRepository extends JpaRepository<SagaState, Long> {
}
