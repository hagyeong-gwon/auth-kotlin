# Tutorial Auth Server

간단한 인증 서버 구축 튜토리얼 프로젝트입니다. Clean Architecture를 기반으로 JWT 인증 및 역할 기반 접근 제어(RBAC)를 구현합니다.

## 📚 목차

- [기술 스택](#-기술-스택)
- [아키텍처](#-아키텍처)
- [패키지 구조](#-패키지-구조)
- [핵심 기능](#-핵심-기능)
- [사용자 역할](#-사용자-역할)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [테스트](#-테스트)

## 기술 스택

- **Language**: Kotlin
- **Framework**: Spring Boot 3.x
- **Database**:
    - MySQL (사용자 정보 저장)
    - Redis (토큰 캐싱)
- **Security**: JWT (JSON Web Token)
- **Testing**:
    - Kotest (테스트 프레임워크)
    - ArchUnit (아키텍처 규칙 검증)

## 아키텍처

이 프로젝트는 **Clean Architecture** 원칙을 따릅니다.

### 핵심 규칙

```
infrastructure (Spring Config) -> adapter (Controller) -> application (UseCase) -> domain (Entity)
```

**의존성 방향:**
- `domain`: 아무것도 의존하지 않음 (순수 Kotlin)
- `application`: domain만 의존
- `adapter`: application + domain 의존
- `infrastructure`: 모든 레이어 조합

### 레이어별 책임

| 레이어 | 책임 | 예시 |
|--------|------|------|
| **Domain** | 비즈니스 규칙, 엔티티 | User, Email, Password |
| **Application** | 유즈케이스, 비즈니스 로직 | SignUpUseCase, LoginService |
| **Adapter** | 외부 세계와의 인터페이스 | REST Controller, JPA Repository |
| **Infrastructure** | 프레임워크 설정 | Spring Config, Security Config |

## 📁 패키지 구조

```
tutorial-auth-server/
├── src/main/kotlin/
│   └── com.example.auth/
│       ├── domain/                    # 🔴 Enterprise Business Rules
│       │   ├── entity/               # User, Token 등
│       │   ├── vo/                   # Email, Password 등
│       │   └── exception/            # Domain 예외
│       │
│       ├── application/               # 🟠 Application Business Rules
│       │   ├── usecase/              # 인터페이스 (input port)
│       │   │   ├── SignUpUseCase.kt
│       │   │   └── RefreshTokenUseCase.kt
│       │   ├── service/              # UseCase 구현
│       │   └── port/                 # output port
│       │       ├── UserRepository.kt
│       │       ├── TokenProvider.kt
│       │       └── PasswordEncoder.kt
│       │
│       ├── adapter/                   # 🟡 Interface Adapters
│       │   ├── input/
│       │   │   └── web/              # REST Controller
│       │   │       ├── dto/          # Request/Response DTO
│       │   │       └── AuthController.kt
│       │   │
│       │   └── output/
│       │       ├── persistence/      # JPA 구현
│       │       │   ├── entity/       # JPA Entity
│       │       │   ├── repository/   # Repository 구현
│       │       │   └── mapper/       # Domain ↔ JPA Entity
│       │       │
│       │       └── security/         # 보안 구현
│       │           ├── JwtTokenProvider.kt
│       │           └── BCryptPasswordEncoder.kt
│       │
│       └── infrastructure/            # 🟢 Frameworks & Drivers
│           ├── config/               # Spring 설정
│           │   ├── SecurityConfig.kt
│           │   ├── JpaConfig.kt
│           │   └── WebConfig.kt
│           └── filter/               # JWT Filter 등
│
└── src/test/kotlin/
    └── com.example.auth/
        ├── domain/                   # Domain 단위 테스트
        ├── application/              # UseCase 단위 테스트
        ├── adapter/                  # 통합 테스트
        ├── e2e/                      # E2E 테스트
        └── architecture/             # ArchUnit 테스트
```

## ✨ 핵심 기능

### 1. 사용자 관리
- 회원가입 (Sign Up)
- 사용자 정보 조회
- 사용자 정보 수정

### 2. 인증/인가
- 로그인 (Login)
- JWT 토큰 발급 (Access Token + Refresh Token)
- 토큰 갱신 (Refresh)
- 로그아웃

### 3. 역할 기반 접근 제어 (RBAC)
- 사용자 역할 관리
- 역할별 권한 제어
- 관리자 권한 세분화

## 👥 사용자 역할

| 역할 | 설명        |
|------|-----------|
| **USER** | 일반 사용자, 기본 기능 사용 가능 |
| **OPERATOR** | 운영자, 콘텐츠 생성 및 관리 |
| **AUDITOR** |  감시자, 읽기 전용 권한 |
| **ADMIN** |  관리자, 전체 시스템 관리 |

### 권한 계층

```
ADMIN (모든 권한)
  ├── OPERATOR (운영 권한)
  ├── AUDITOR (조회 권한)
  └── USER (기본 권한)
```

## 🚀 시작하기

### 사전 요구사항

- JDK 17 이상
- Docker & Docker Compose (MySQL, Redis 실행용)
- Gradle 8.x

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/hagyeong-gwon/auth-kotlin.git
cd tutorial-auth-server
```

2. **데이터베이스 실행**
```bash
docker-compose up -d
```

3. **애플리케이션 빌드**
```bash
./gradlew build
```

4. **애플리케이션 실행**
```bash
./gradlew bootRun
```

5. **접속 확인**
```
http://localhost:8080
```

### 환경 변수 설정

`application.yml` 파일을 생성하고 다음 설정을 추가하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth_db
    username: root
    password: password
  
  data:
    redis:
      host: localhost
      port: 6379

jwt:
  secret: your-secret-key-here
  access-token-expiration: 3600000  # 1시간
  refresh-token-expiration: 604800000  # 7일
```

## 🧪 테스트

### 전체 테스트 실행
```bash
./gradlew test
```

### 테스트 커버리지 확인
```bash
./gradlew jacocoTestReport
```

### ArchUnit 테스트 (아키텍처 규칙 검증)
```bash
./gradlew test --tests "*ArchitectureTest"
```

### 테스트 구조

```
📦 테스트 유형
├── Unit Test (70%)
│   ├── Domain Test: 순수 비즈니스 로직
│   └── UseCase Test: Mock을 사용한 단위 테스트
│
├── Integration Test (20%)
│   ├── Controller Test: MockMvc 기반
│   ├── Repository Test: @DataJpaTest
│   └── Security Test: JWT 검증
│
└── E2E Test (10%)
    └── 전체 플로우 테스트: @SpringBootTest
```

## 학습 목표

- Clean Architecture 구조 설계
- Spring Security와 JWT 통합
- 역할 기반 접근 제어 (RBAC) 구현
- Kotlin + Spring Boot 개발
- 테스트 주도 개발 (TDD)
- 계층별 테스트 전략
- Redis를 활용한 캐싱 전략