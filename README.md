# Commit Message Convention
커밋 메시지 양식
Feat: "로그인 함수 추가" -> 제목

로그인 요청을 위한 함수 구현 -> 본문

## 제목 작성 두 가지 방식
둘 중 본인이 편한 방식으로 진행해주시면 됩니다.
1. Feat: "로그인 함수 추가"
2. be-feat-1 : "작업내용" -> 강사님 추천 방식
Commit Type
Feat : 새로운 기능 추가
Fix : 버그 수정
Env : 개발 환경 관련 설정
Style : 코드 스타일 수정 (세미 콜론, 인덴트 등의 스타일적인 부분만)
Refactor : 코드 리팩토링 (더 효율적인 코드로 변경 등)
Design : CSS 등 디자인 추가/수정
Comment : 주석 추가/수정
Docs : 내부 문서 추가/수정
Test : 테스트 추가/수정
Chore : 빌드 관련 코드 수정
Rename : 파일 및 폴더명 수정
Remove : 파일 삭제

### Commit Message Convention의 자세한 설명은 아래 링크를 참고바랍니다.
https://projectlog.tistory.com/57

---



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

![데이터베이스 ERD](https://i.postimg.cc/q7Ytpb4K/2025-07-18-3-45-19.png)

---
### 전체 시스템 흐름

`Client (Next.js)` ↔️ `API (Spring Boot)` ↔️ `Database (MySQL)`

---
