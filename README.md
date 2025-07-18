# 1차 프로젝트: Caffe 온라인 스토어

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Next.js](https://img.shields.io/badge/Next.js-22.x-black?logo=next.js)](https://nextjs.org/)
[![JPA](https://img.shields.io/badge/JPA-Hibernate-blue.svg)](https://hibernate.org/orm/)

프로그래머스 백엔드 데브코스 6기 8회차 12팀의 1차 프로젝트입니다. Spring Boot 기반의 백엔드와 Next.js로 구현된 프론트엔드를 통해, 상품 관리부터 주문, 결제, 배송까지 이어지는 완전한 E-Commerce 시스템을 개발했습니다.

---
#### 팀원

## 👨‍👩‍👧‍👦 팀원

| 역할 | 이름 | GitHub 프로필 |
| :--- | :--- | :--- |
| **팀장** | `[김유미]` | `[GitHub 프로필 링크]` |
| **팀원** | `[윤지혜]` | `[GitHub 프로필 링크]` |
| **팀원** | `[이준모]` | `[GitHub 프로필 링크]` |
| **팀원** | `[임종현]` | `[GitHub 프로필 링크]` |
| **팀원** | `[임홍담]` | `[GitHub 프로필 링크]` |

---
## 📝 프로젝트 개요

**Caffe 온라인 스토어**는 사용자가 커피 원두 상품을 구매할 수 있는 온라인 쇼핑몰입니다. 이 프로젝트는 MSA(Microservice Architecture)를 지향하는 도메인 주도 설계(DDD)를 학습하고, 실제 서비스와 유사한 복잡도를 가진 시스템을 구축하는 것을 목표로 합니다. 백엔드는 Spring Boot를 통해 RESTful API를 제공하며, 프론트엔드는 Next.js를 사용하여 뛰어난 사용자 경험을 제공하는 서버 사이드 렌더링(SSR) 및 정적 사이트 생성(SSG)을 구현합니다.

---

## 🚀 주요 기능

* **회원 (Member)**: 회원가입, 로그인, 마이페이지, 주소록 관리
* **상품 (Product)**: 상품 목록 조회, 상세 정보 조회, 카테고리별 분류
* **주문 (Purchase)**: 장바구니, 주문 생성, 주문 내역 조회
* **결제 (Payment)**: 외부 결제 API 연동을 통한 결제 처리, 결제 상태 관리
* **배송 (Shipping)**: 배송지 정보 관리, 배송 상태 추적

---

## 🏛️ 기술 스택

### Backend (`backend` 폴더)

* **Language**: Java 21
* **Framework**: Spring Boot 3.5.3
* **Data**: Spring Data JPA (Hibernate)
* **Database**: H2 (개발용/운영용)
* **Build Tool**: Gradle

### Frontend (`frontend` 폴더)

* **Framework**: Next.js
* **Language**: TypeScript
* **Styling**: Tailwind CSS

---

## 🏗️ 아키텍처

### 백엔드 도메인 구조 (`java.com.caffe.domain`)

백엔드는 각 기능의 독립성을 높이기 위해 도메인별로 패키지를 명확히 분리했습니다.

* **`member`**: 사용자 인증 및 회원 정보 관리
* **`product`**: 상품 정보 및 재고 관리
* **`purchase`**: 주문 생성 및 주문 내역 관리
* **`payment`**: 결제 요청 및 결과 처리
* **`shipping`**: 배송 정보 및 상태 관리
* **`global`**: 예외 처리, 공통 설정 등 전역적으로 사용되는 모듈

### DB ERD

```plantuml
@startuml
!theme plain

entity MEMBER {
    id: Integer <<PK>>
    email: String <<UK>> "이메일(아이디)"
    password: String
    username: String "이름"
    role: String "권한 (USER, ADMIN)"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity PRODUCT {
    id: Integer <<PK>>
    product_name: String "상품명"
    price: Integer "가격"
    total_quantity: Integer "재고 수량"
    description: String "상품 설명"
    image_url: String "이미지 정보"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity PURCHASE {
    id: Integer <<PK>>
    user_email: String "주문자 이메일"
    totalPrice: Integer "총 주문 가격"
    status: String "주문 상태"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity PURCHASE_ITEM {
    id: Integer <<PK>>
    purchase_id: Integer <<FK>> "주문 ID"
    product_id: Integer <<FK>> "상품 ID"
    quantity: Integer "주문 수량"
    price: Integer "주문 당시 가격"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity PAYMENT {
    id: Integer <<PK>>
    purchase_id: Integer <<FK>> "주문 ID"
    payment_option_id: Integer <<FK>> "결제 수단 정보"
    payment_info: String "결제 정보"
    status: String "결제 상태 (PENDING, CANCELED, SUCCESS, FAILED)"
    amount: Integer "결제 금액"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity PAYMENT_OPTION {
    id: Integer <<PK>>
    parent_id: Integer <<FK>> "상위 결제 옵션 ID"
    code: Integer "결제 수단 코드"
    name: String "결제 수단 명"
    type: String "결제 수단의 타입"
    sort_seq: Integer "정렬 순서"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

entity SHIPPING {
    id: Integer <<PK>>
    purchase_id: Integer <<FK>> "주문 ID"
    contact_name: String "수령인 이름"
    contact_number: String "수령인 전화 번호"
    address: String "배송 주소"
    postcode: String "우편 번호"
    carrier: String "배송 업체"
    email: String "수령인 이메일"
    status: String "배송 상태 (PREPARING, IN_TRANSIT...)"
    createDate: LocalDateTime
    modifyDate: LocalDateTime
}

MEMBER ||--o{ PURCHASE : has
PURCHASE ||--|{ PURCHASE_ITEM : contains
PRODUCT ||--o{ PURCHASE_ITEM : "is part of"
PURCHASE ||--|| PAYMENT : "is paid by"
PURCHASE ||--|| SHIPPING : "is shipped via"
PAYMENT ||--|{ PAYMENT_OPTION : contains
@enduml

---
### 전체 시스템 흐름

`Client (Next.js)` ↔️ `API (Spring Boot)` ↔️ `Database (MySQL)`

---
