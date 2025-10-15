package com.example.orchestration.controller;

import com.example.orchestration.dto.OrderRequest;
import com.example.orchestration.service.SagaOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saga")
@RequiredArgsConstructor
public class OrchestratorController {
	private final SagaOrchestrator sagaOrchestrator;
	@PostMapping("/order")
	public String startSaga(@RequestBody OrderRequest orderRequest) {
		sagaOrchestrator.startSaga(orderRequest);
		return "saga process started!";
	}
}
