# KiKiHi Backend Server

커스텀 키보드 플랫폼을 위한 Spring Boot 기반 백엔드 서버

## 📋 목차

- [개요](#개요)
- [기술 스택](#기술-스택)
- [아키텍처](#아키텍처)
- [MongoDB 설정 및 운영](#mongodb-설정-및-운영)
- [ELK Stack 설정 및 운영](#elk-stack-설정-및-운영)
- [데이터 동기화: Monstache](#데이터-동기화-monstache)
- [로컬 개발 환경 설정](#로컬-개발-환경-설정)
- [주요 명령어](#주요-명령어)

## 개요

KiKiHi Backend는 커스텀 키보드 부품 검색, 추천, 북마크 기능을 제공하는 플랫폼입니다.
멀티 데이터베이스 전략을 사용하며, MongoDB에서 Elasticsearch로의 실시간 데이터 동기화를 통해 강력한 검색 기능을 제공합니다.

## 기술 스택

### Core
- **Java 17** / **Spring Boot 3.4.3**
- **Gradle 8.13**

### 데이터베이스
- **MySQL 8.0**: 사용자, 북마크, 커스텀 키보드 데이터
- **MongoDB**: 상품 카탈로그 (Replica Set 구성)
- **Redis 7.2.5**: JWT 토큰 관리, 세션 캐시
- **Elasticsearch 8.x**: 검색 엔진 (analysis-nori 플러그인)

### 모니터링 & 로깅
- **ELK Stack**
  - Elasticsearch: 로그 저장 및 검색
  - Logstash: 로그 수집 및 변환
  - Kibana: 로그 시각화 및 분석

### 데이터 동기화
- **Monstache**: MongoDB → Elasticsearch 실시간 동기화

### 보안 & 인증
- **Spring Security 6.x**
- **JWT** (jjwt 0.11.5)
- **OAuth2** (Google, Kakao)

## 아키텍처

### 멀티 데이터베이스 전략

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   MySQL     │     │  MongoDB    │     │    Redis    │
│  (JPA/RDB)  │     │ (Products)  │     │  (Session)  │
└─────────────┘     └──────┬──────┘     └─────────────┘
                           │
                      Monstache
                      (Change Stream)
                           │
                           ▼
                    ┌─────────────┐
                    │Elasticsearch│
                    │  (Search)   │
                    └─────────────┘
```

- **MySQL**: 관계형 데이터 (User, Bookmark, CustomKeyboard)
- **MongoDB**: 유연한 스키마가 필요한 상품 데이터 (Product)
- **Elasticsearch**: 한글 형태소 분석 기반 상품 검색
- **Redis**: JWT 리프레시 토큰 블랙리스트 관리

### 데이터 흐름

```
Application → MongoDB (상품 등록/수정/삭제)
                 ↓
        Change Stream (실시간 감지)
                 ↓
            Monstache (동기화)
                 ↓
         Elasticsearch (검색 인덱스 업데이트)
                 ↓
        Application → 검색 API 제공
```

## MongoDB 설정 및 운영

### Replica Set 구성

MongoDB는 **Replica Set** 모드로 구성되어 Change Stream을 지원합니다:

```yaml
services:
  mongodb1:
    image: mongo:latest
    ports:
      - "27017:27017"
    command:
      - mongod
      - "--replSet"
      - "replication"
      - "--keyFile"
      - "/etc/mongodb.key"
      - "--bind_ip_all"

  mongodb2:
    image: mongo:latest
    ports:
      - "27018:27017"
    command:
      - mongod
      - "--replSet"
      - "replication"
      - "--keyFile"
      - "/etc/mongodb.key"
      - "--bind_ip_all"
```

### Replica Set 초기화

Replica Set은 `mongosetup` 컨테이너가 자동으로 초기화합니다:

```javascript
rs.initiate({
  _id: "replication",
  members: [
    { _id: 0, host: "mongodb1:27017" },
    { _id: 1, host: "mongodb2:27017" }
  ]
})
```

## ELK Stack 설정 및 운영

### Elasticsearch 설정

#### 보안 인증서 생성

Elasticsearch는 TLS/SSL을 사용하여 보안 통신을 수행합니다. `setup` 컨테이너가 자동으로 인증서를 생성합니다:

```bash
# CA 인증서 생성
elasticsearch-certutil ca --silent --pem -out config/certs/ca.zip

# 노드별 인증서 생성 (es01, kibana, monstache)
elasticsearch-certutil cert --silent --pem -out config/certs/certs.zip
```

#### Elasticsearch 구성

```yaml
es01:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.x
  ports:
    - "9200:9200"
  environment:
    - node.name=es01
    - cluster.name=kikihi-cluster
    - discovery.type=single-node
    - ELASTIC_PASSWORD=${ELASTIC_PASSWORD}
    - xpack.security.enabled=true
    - xpack.security.http.ssl.enabled=true
```

#### analysis-nori 플러그인 (한글 형태소 분석)

Elasticsearch 시작 시 자동으로 `analysis-nori` 플러그인을 설치하여 한글 검색을 지원합니다:

```bash
# Dockerfile entrypoint에서 자동 실행
bin/elasticsearch-plugin install -b analysis-nori
```

#### 인덱스 매핑 예시

```json
{
  "settings": {
    "analysis": {
      "analyzer": {
        "korean_analyzer": {
          "type": "custom",
          "tokenizer": "nori_tokenizer",
          "filter": ["lowercase", "nori_readingform"]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "korean_analyzer"
      },
      "category": {
        "type": "keyword"
      },
      "price": {
        "type": "integer"
      }
    }
  }
}
```

### Logstash 설정

#### Logstash 파이프라인 구성

Logstash는 애플리케이션 로그를 Elasticsearch로 전송합니다:

```ruby
# docker/logstash/logstash.conf
input {
  tcp {
    port => 5044
    codec => json
  }
}

filter {
  # JSON 로그 파싱
  if [message] =~ /^\{/ {
    json {
      source => "message"
    }
  }

  # 타임스탬프 설정
  date {
    match => ["timestamp", "ISO8601"]
    target => "@timestamp"
  }
}

output {
  elasticsearch {
    hosts => ["https://es01:9200"]
    index => "app-logs-%{+YYYY.MM.dd}"
    user => "elastic"
    password => "${ELASTIC_PASSWORD}"
    cacert => "/usr/share/logstash/certs/ca/ca.crt"
  }

  stdout {
    codec => rubydebug
  }
}
```

#### Spring Boot Logback 설정

```xml
<!-- src/main/resources/logback-spring.xml -->
<configuration>
  <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
    <destination>localhost:5044</destination>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
      <customFields>{"app":"kikihi-backend"}</customFields>
    </encoder>
  </appender>

  <root level="INFO">
    <appender-ref ref="LOGSTASH"/>
  </root>
</configuration>
```

### Kibana 설정

#### 주요 활용

1. **Discover**: 실시간 로그 검색 및 필터링
2. **Dashboard**: 커스텀 대시보드 생성
   - API 요청 수 추이
   - 에러 발생 통계
   - 응답 시간 분석
3. **Alerting**: 특정 조건 발생 시 알림 설정

## 라이선스

이 프로젝트는 KiKiHi 팀의 소유입니다.
