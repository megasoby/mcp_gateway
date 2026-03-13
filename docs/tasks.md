# Task List
# MCP Gateway 개발 작업 목록

## Phase 1 — MCP Gateway 기본 구조 ✅ 완료

### 환경 설정
- [x] Java 17 + Gradle 프로젝트 생성
- [x] `build.gradle` 의존성 설정 (Spring Boot 3.4.5, Spring AI MCP 1.0.0)
- [x] `application.yml` 서버 설정 (포트 8082, SSE 방식)
- [x] Gradle Wrapper 생성 (`gradlew`)
- [x] 서버 시작 스크립트 작성 (`start.sh`)

### MCP 서버 구현
- [x] `McpGatewayApplication.java` 메인 클래스 생성
- [x] `RestClientConfig.java` — RestClient 빈 등록
- [x] `RestClientConfig.java` — `ToolCallbackProvider` 빈 등록
- [x] `InventoryTool.java` — `queryInventory` 도구 구현 (재고 조회)
- [x] `InventoryTool.java` — `queryPrice` 도구 구현 (판매가 조회)

### 클라이언트 연결
- [x] `.cursor/mcp.json` — Cursor IDE SSE 연결 설정
- [x] `~/.cursor/mcp.json` — Cursor 글로벌 MCP 등록
- [x] `~/.gemini/settings.json` — Gemini CLI 연결 설정
- [x] Gemini CLI 설치 (`npm install -g @google/gemini-cli`)

### 테스트 및 검증
- [x] curl로 SSE 엔드포인트 동작 확인 (`GET /sse`)
- [x] MCP 프로토콜 전체 흐름 테스트 (initialize → tools/list → tools/call)
- [x] Cursor에서 재고 조회 테스트
- [x] Cursor에서 판매가 + 재고 동시 조회 테스트
- [x] Gemini CLI에서 재고 조회 테스트
- [x] `start.sh` jar 실행 방식으로 변경

### 문서화
- [x] `docs/prd.md` 작성
- [x] `docs/tasks.md` 작성

---

## Phase 2 — 도구 확장 🔜 예정

### 상품 검색 도구
- [ ] 상품 검색 API 확인 및 테스트
- [ ] `ProductTool.java` 클래스 생성
- [ ] `searchProduct` 도구 구현 (키워드 검색)
- [ ] `getProductDetail` 도구 구현 (상품 상세 조회)
- [ ] `RestClientConfig.java`에 `ProductTool` 등록
- [ ] 빌드 및 테스트

### 주문 조회 도구
- [ ] 주문 조회 API 확인 및 테스트
- [ ] `OrderTool.java` 클래스 생성
- [ ] `getOrderStatus` 도구 구현 (주문 상태 조회)
- [ ] `RestClientConfig.java`에 `OrderTool` 등록
- [ ] 빌드 및 테스트

### 도메인별 MCP 서버 분리 (도구 30개 이상 시)
- [ ] 재고/가격 MCP 서버 분리 (포트 8082)
- [ ] 상품 MCP 서버 분리 (포트 8083)
- [ ] 주문 MCP 서버 분리 (포트 8084)
- [ ] 각 클라이언트 설정 파일 업데이트

---

## Phase 3 — UCP 연동 🔜 예정 (한국 지원 시)

### 사전 준비
- [ ] Google Merchant Center 계정 확인
- [ ] 상품 피드 정비 (상품명, 가격, 이미지, 배송/반품 정책)
- [ ] `native_commerce` 속성 피드에 추가
- [ ] Google 대기자 명단 신청 (https://support.google.com/merchants/contact/ucp_integration_interest)
- [ ] Google 담당자 미팅 및 승인 대기

### Google Pay 설정
- [ ] PG사 Google Pay 토큰화 지원 여부 확인
- [ ] Google Pay & Wallet Console 접속 및 Merchant ID 발급

### UCP REST API 구현
- [ ] `UcpCheckoutController.java` 생성
- [ ] `POST /ucp/checkout-sessions` — 체크아웃 세션 생성
  - [ ] 실시간 재고 확인 로직 (queryInventory 재활용)
  - [ ] 실시간 가격 확인 로직 (queryPrice 재활용)
  - [ ] 체크아웃 세션 DB 저장
- [ ] `GET /ucp/checkout-sessions/{id}` — 세션 조회
- [ ] `PUT /ucp/checkout-sessions/{id}` — 세션 업데이트 (배송지, 수량)
- [ ] `POST /ucp/checkout-sessions/{id}/complete` — 결제 완료
  - [ ] 최종 재고 차감 로직
  - [ ] 주문 생성 API 호출
  - [ ] Google Pay 결제 처리
- [ ] `POST /ucp/checkout-sessions/{id}/cancel` — 주문 취소

### UCP 디스커버리 프로필
- [ ] `/.well-known/ucp` 엔드포인트 구현
- [ ] UCP 프로필 JSON 작성 (서비스 정보, 엔드포인트 URL)

### 테스트 및 검수
- [ ] 전체 체크아웃 흐름 테스트 (생성 → 수정 → 완료)
- [ ] 재고 없을 때 에러 처리 테스트
- [ ] 결제 실패 시 롤백 테스트
- [ ] HTTPS + TLS 1.3 설정
- [ ] Google 검수 제출

---

## Phase 4 — ACP 연동 🔜 예정

### ACP 구현
- [ ] OpenAI ACP 스펙 분석
- [ ] Stripe 결제 연동
- [ ] ACP 체크아웃 엔드포인트 구현
- [ ] ChatGPT Instant Checkout 테스트

---

## 참고

### 서버 실행
```bash
# 첫 실행 또는 코드 변경 후
./gradlew bootJar && ./start.sh

# 코드 변경 없을 때
./start.sh
```

### Gemini CLI 실행
```bash
# 대화 모드
GEMINI_API_KEY=AIzaSyCSYjAIoaJrRkGZDAoqlZq1_5s57h2myNI gemini --no-sandbox

# 한 줄 질문
echo '질문' | GEMINI_API_KEY=AIzaSyCSYjAIoaJrRkGZDAoqlZq1_5s57h2myNI gemini --no-sandbox
```
