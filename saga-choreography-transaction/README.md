## Saga Choreography Pattern 예제

이 프로젝트는 마이크로서비스 아키텍처에서 분산 트랜잭션을 관리하기 위한 **사가 코레오그래피 (Saga Choreography) 패턴**의 구현 예제입니다.

- **Order Service**: 주문을 생성하고 `order-created` 이벤트를 발행합니다.
- **Payment Service**: 주문 생성 이벤트를 구독하여 결제를 처리하고, `payment-events`를 발행합니다.

두 서비스는 중앙 조정자 없이 Kafka 메시징 큐를 통해 이벤트를 주고받으며 데이터의 최종 일관성을 유지합니다.

-----

## 프로젝트 아키텍처

1.  **주문 생성**: 사용자가 **Order Service**에 API를 요청하여 주문을 생성합니다. 주문은 `PENDING` (대기) 상태로 DB에 저장됩니다.
2.  **이벤트 발행**: **Order Service**는 주문이 생성되었다는 의미의 `order-created` 이벤트를 **Kafka**로 발행합니다.
3.  **이벤트 구독 및 처리**: **Payment Service**가 이 이벤트를 구독하여 받고, 결제를 시도합니다.
4.  **결과 발행**: **Payment Service**는 결제 결과를 `payment-events`라는 토픽으로 **Kafka**에 발행합니다. (성공 시 `COMPLETED`, 실패 시 `CANCELLED`)
5.  **상태 업데이트**: **Order Service**가 결제 결과 이벤트를 구독하여 받고, 주문의 최종 상태를 DB에 업데이트합니다.

-----

## 테스트 방법

`Order Service` (포트 8080)로 HTTP POST 요청을 보내 테스트합니다. 결제 성공/실패 여부는 **주문 수량**에 따라 결정됩니다.

#### 1\. 성공 케이스 (수량 \< 10)

주문 수량을 10개 미만으로 요청하면 결제가 성공하고, 최종 주문 상태는 **COMPLETED**가 됩니다.

```bash
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{"product": "Laptop", "quantity": 5}'
```

#### 2\. 실패 케이스 (수량 \>= 10)

주문 수량을 10개 이상으로 요청하면 결제가 실패하고, 최종 주문 상태는 **FAILED**가 됩니다.

```bash
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{"product": "Monitor", "quantity": 15}'
```