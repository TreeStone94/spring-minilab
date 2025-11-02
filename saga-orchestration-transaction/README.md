## Saga Orchestration Pattern 예제

이 프로젝트는 마이크로서비스 아키텍처에서 분산 트랜잭션을 관리하기 위한 **사가 오케스트레이션 (Saga Orchestration) 패턴**의 구현 예제입니다.

- **Orchestration Service**: 전체 트랜잭션을 조정하고 각 서비스에 명령을 내립니다.
- **Order Service**: 주문을 생성하고 Orchestration Service에 결과를 보고합니다.
- **Payment Service**: 결제를 처리하고 Orchestration Service에 결과를 보고합니다.
- **Stock Service**: 재고를 관리하고 Orchestration Service에 결과를 보고합니다.

모든 서비스는 중앙 조정자인 **Orchestration Service**의 명령에 따라 동작하며, Kafka 메시징 큐를 통해 통신하여 데이터의 일관성을 유지합니다.

-----

## 프로젝트 아키텍처

1.  **주문 시작**: 사용자가 **Orchestration Service**에 API를 요청하여 Saga 트랜잭션을 시작합니다.
2.  **주문 생성 요청**: **Orchestration Service**는 **Order Service**에 주문 생성을 요청합니다.
3.  **결제 요청**: 주문이 성공하면 **Orchestration Service**는 **Payment Service**에 결제를 요청합니다.
4.  **재고 감소 요청**: 결제가 성공하면 **Orchestration Service**는 **Stock Service**에 재고 감소를 요청합니다.
5.  **Saga 완료**: 모든 단계가 성공하면 Saga가 완료됩니다. 단계 중 하나라도 실패하면 **Orchestration Service**가 보상 트랜잭션을 시작하여 이전 단계를 롤백합니다.

-----

## 테스트 방법

`Orchestration Service` (포트 8080)로 HTTP POST 요청을 보내 테스트합니다. 각 서비스의 성공/실패 여부는 요청 파라미터에 따라 결정됩니다.

#### 1\. 성공 케이스

모든 서비스가 성공적으로 트랜잭션을 완료하는 시나리오입니다.

```bash
curl -X POST http://localhost:8080/saga/order \
-H "Content-Type: application/json" \
-d '{"productId": 1, "quantity": 5}'
```

#### 2\. 실패 케이스 (예: 결제 실패)

결제 서비스에서 오류가 발생하여 보상 트랜잭션이 실행되는 시나리오입니다.

```bash
# (예: sagaId가 홀수 일때 결제 실패)
curl -X POST http://localhost:8080/saga/order \
-H "Content-Type: application/json" \
-d '{"productId": 1, "quantity": 5}'
```
