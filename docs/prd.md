# PRD (Product Requirements Document)
# MCP Gateway — AI 에이전트 연결 게이트웨이

## 1. 개요

### 목적
사내 API들을 MCP(Model Context Protocol) 표준으로 노출하여, 다양한 AI 에이전트(Gemini, ChatGPT, Claude 등)가 우리 회사 서비스에 접근할 수 있도록 하는 게이트웨이를 구축한다.

### 배경
- AI 에이전트 시장이 빠르게 성장하면서 Google UCP, OpenAI ACP 등 AI 커머스 프로토콜이 등장
- 각 AI 플랫폼마다 별도 연동을 개발하는 것은 비효율적
- MCP 표준으로 한번 구현하면 모든 AI 에이전트에서 재사용 가능
- 장기적으로 Gemini/ChatGPT 웹에서 상품 검색 및 구매 연동(UCP/ACP)을 위한 기반 마련

### 목표
1. **단기**: 사내 API를 MCP 도구로 노출 → 내부 AI 업무 자동화
2. **중기**: UCP(Google), ACP(OpenAI) 연동 → AI 커머스 플랫폼 연결
3. **장기**: Gemini/ChatGPT 웹에서 상품 검색 + 구매 가능

---

## 2. 사용자 스토리

### 내부 사용자 (개발자 / MD / 운영팀)
- `AS A` 개발자로서 `I WANT` Cursor IDE에서 자연어로 재고/가격을 조회하고 싶다 `SO THAT` 별도 API 호출 없이 업무를 처리할 수 있다
- `AS A` MD로서 `I WANT` Gemini CLI에서 "이 상품 재고 몇 개야?" 라고 물어보고 싶다 `SO THAT` 시스템에 직접 접속하지 않아도 된다
- `AS A` 운영팀으로서 `I WANT` AI 에이전트를 통해 주문/재고 현황을 파악하고 싶다 `SO THAT` 빠르게 의사결정을 할 수 있다

### 외부 사용자 (일반 소비자, 향후)
- `AS A` 소비자로서 `I WANT` Gemini 앱에서 상품을 검색하고 바로 구매하고 싶다 `SO THAT` 별도 앱 없이 AI 안에서 쇼핑을 완료할 수 있다

---

## 3. 핵심 기능

### Phase 1 (완료) — MCP Gateway 기본 구조
| 기능 | 설명 | 상태 |
|------|------|------|
| MCP 서버 구동 | SSE 방식으로 MCP 서버 실행 (포트 8082) | ✅ 완료 |
| 재고 조회 도구 | `queryInventory` — 상품ID, 판매처, 단위상품ID로 재고 조회 | ✅ 완료 |
| 판매가 조회 도구 | `queryPrice` — 상품ID, 판매처, 단위상품ID로 판매가 조회 | ✅ 완료 |
| Cursor 연결 | `.cursor/mcp.json`으로 Cursor IDE 연결 | ✅ 완료 |
| Gemini CLI 연결 | `~/.gemini/settings.json`으로 Gemini CLI 연결 | ✅ 완료 |

### Phase 2 (예정) — 도구 확장
| 기능 | 설명 | 상태 |
|------|------|------|
| 상품 검색 도구 | 키워드로 상품 검색 | 🔜 예정 |
| 주문 조회 도구 | 주문 번호로 주문 상태 조회 | 🔜 예정 |
| 도메인별 서버 분리 | 재고/가격/주문/상품 MCP 서버 분리 | 🔜 예정 |

### Phase 3 (예정) — UCP/ACP 연동
| 기능 | 설명 | 상태 |
|------|------|------|
| UCP 체크아웃 API | `create/get/update/complete/cancel_checkout` 5개 구현 | 🔜 예정 |
| `/.well-known/ucp` 프로필 | Gemini가 서버를 발견하기 위한 디스커버리 파일 | 🔜 예정 |
| Google Pay 연동 | 결제 처리 | 🔜 예정 |
| ACP 연동 | OpenAI/Stripe 규격 체크아웃 API 구현 | 🔜 예정 |

---

## 4. 기술 스택

| 항목 | 기술 |
|------|------|
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.4.5 |
| **MCP 라이브러리** | Spring AI MCP 1.0.0 (`spring-ai-starter-mcp-server-webmvc`) |
| **빌드 도구** | Gradle 8.12 |
| **전송 방식** | SSE (Server-Sent Events) |
| **포트** | 8082 |
| **HTTP 클라이언트** | Spring RestClient |

---

## 5. 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                    AI 에이전트 (클라이언트)                │
│                                                         │
│  Cursor IDE    Gemini CLI    Claude Desktop    ChatGPT  │
└────────────────────────┬────────────────────────────────┘
                         │ MCP (SSE / Streamable HTTP)
                         │
┌────────────────────────▼────────────────────────────────┐
│                  MCP Gateway (Spring Boot)               │
│                       :8082                             │
│                                                         │
│  ┌─────────────────┐  ┌─────────────────┐              │
│  │ InventoryTool   │  │ PriceTool       │  ...         │
│  │ queryInventory  │  │ queryPrice      │              │
│  └────────┬────────┘  └────────┬────────┘              │
└───────────┼────────────────────┼────────────────────────┘
            │ RestClient         │ RestClient
            │                   │
┌───────────▼────────────────────▼────────────────────────┐
│                    사내 API 서버                          │
│                                                         │
│  재고 API              판매가 API           주문 API 등  │
└─────────────────────────────────────────────────────────┘
```

---

## 6. 비기능 요구사항

| 항목 | 요구사항 |
|------|---------|
| **보안** | 내부망 전용 운영 (외부 노출 시 인증 추가 필요) |
| **성능** | API 응답 시간 3초 이내 |
| **가용성** | 서버 장애 시 에러 메시지 반환 |
| **확장성** | 새로운 API 추가 시 Tool 클래스에 메서드만 추가 |

---

## 7. 제약사항

- Google UCP 한국 지원 미확정 (현재 미국 중심)
- Gemini 웹/앱 직접 연결은 UCP 승인 후 가능
- 무료 Gemini API는 일일 250건 제한
