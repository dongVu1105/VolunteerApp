# Profile Service

Service quản lý hồ sơ người dùng trong hệ thống **VolunteerHub** - một phần của kiến trúc microservices.

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Các chức năng chính](#các-chức-năng-chính)
- [Điểm nổi bật](#điểm-nổi-bật)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [API Documentation](#api-documentation)
- [Cấu hình](#cấu-hình)

## 🎯 Tổng quan

**Profile Service** là microservice chịu trách nhiệm quản lý thông tin hồ sơ người dùng trong hệ thống VolunteerHub. Service này sử dụng **Neo4j** làm cơ sở dữ liệu đồ thị để lưu trữ và quản lý dữ liệu hồ sơ, cho phép xây dựng các mối quan hệ phức tạp giữa người dùng trong tương lai.

## 🚀 Công nghệ sử dụng

### Core Technologies
- **Java 21** - Phiên bản Java mới nhất với các tính năng hiện đại
- **Spring Boot 3.5.7** - Framework chính cho backend
- **Spring Cloud 2025.0.0** - Hỗ trợ microservices architecture
- **Neo4j** - Cơ sở dữ liệu đồ thị NoSQL

### Security & Authentication
- **Spring OAuth2 Resource Server** - Xác thực và phân quyền dựa trên JWT
- **Custom JWT Decoder** - Giải mã và xác thực JWT token tùy chỉnh
- **BCrypt Password Encoder** - Mã hóa mật khẩu an toàn

### Data & Mapping
- **Spring Data Neo4j** - ORM cho Neo4j database
- **MapStruct 1.5.5** - Mapping giữa các DTO và Entity
- **Lombok 1.18.30** - Giảm boilerplate code

### Communication
- **Spring Cloud OpenFeign** - HTTP client cho inter-service communication
- **Feign Form Spring 3.8.0** - Hỗ trợ multipart/form-data

### Build Tool
- **Maven** - Quản lý dependencies và build project

## ⚙️ Các chức năng chính

### 1. **Quản lý hồ sơ người dùng**
- ✅ Tạo hồ sơ người dùng mới (thông qua internal API)
- ✅ Tìm kiếm hồ sơ theo User ID
- ✅ Tìm kiếm hồ sơ theo Username
- ✅ Lấy danh sách hồ sơ theo nhiều User ID

### 2. **Public API Endpoints**
```
GET /profile/user/{userId}           - Tìm profile theo user ID
GET /profile/user/find/{username}    - Tìm profile theo username
```

### 3. **Internal API Endpoints** (Dành cho giao tiếp giữa các services)
```
POST /profile/internal/user                - Tạo profile mới
GET  /profile/internal/user/{userId}       - Tìm profile theo user ID
POST /profile/internal/userId-list         - Lấy danh sách profile theo user ID list
GET  /profile/internal/find/{username}     - Tìm profile theo username
```

### 4. **Thông tin hồ sơ người dùng**
Hệ thống quản lý các thông tin sau:
- Thông tin cá nhân: Họ tên, ngày sinh, giới tính
- Thông tin liên hệ: Số điện thoại
- Thông tin định danh: Số CMND/CCCD
- Ảnh đại diện: URL avatar (tích hợp với File Service)
- Liên kết: User ID, Username

## ✨ Điểm nổi bật

### 1. **Sử dụng Neo4j Graph Database**
- 📊 Cơ sở dữ liệu đồ thị cho phép mô hình hóa và truy vấn các mối quan hệ phức tạp
- 🔗 Tối ưu cho các tính năng social network trong tương lai (follow, friend connections)
- ⚡ Hiệu suất cao khi truy vấn các mối quan hệ nhiều cấp

### 2. **Kiến trúc Microservices**
- 🔐 Phân tách rõ ràng giữa Public API và Internal API
- 🔒 Security được cấu hình riêng biệt cho từng loại endpoint
- 🌐 Sử dụng OpenFeign để giao tiếp với các services khác (File Service)

### 3. **Security & Authentication**
- 🛡️ JWT-based authentication với custom decoder
- 🔑 Public endpoints không yêu cầu authentication (cho phép các services khác gọi)
- 👥 Authenticated endpoints yêu cầu JWT token hợp lệ
- 🚫 Custom authentication entry point xử lý lỗi 401 Unauthorized

### 4. **Code Quality & Maintainability**
- 🧹 Clean Architecture với tách biệt rõ ràng các layer (Controller, Service, Repository)
- 🔄 MapStruct tự động generate mapping code, giảm lỗi và tăng performance
- 📝 Lombok giảm boilerplate code
- 📋 OpenAPI specification (profile_openapi.yaml) cho API documentation

### 5. **Integration với hệ thống**
- 🖼️ Tích hợp với File Service để quản lý avatar
- 🔗 Default avatar được cấu hình sẵn từ File Service
- 📡 Hỗ trợ batch operations (lấy nhiều profiles cùng lúc)

### 6. **Response Format chuẩn**
```json
{
  "code": 0,
  "message": "success!",
  "data": {
    // User profile data
  }
}
```
- Định dạng response thống nhất với `ApiResponse` wrapper
- Dễ dàng xử lý error và success cases

### 7. **Developer Experience**
- 🔧 Hot reload với Spring Boot DevTools
- 📝 Comprehensive DTO validation
- 🧪 Test-ready structure
- 📚 Well-documented API với OpenAPI 3.0

## 📁 Cấu trúc dự án

```
profile-service/
├── src/
│   ├── main/
│   │   ├── java/com/dongVu1105/profile_service/
│   │   │   ├── configuration/          # Security & Application configs
│   │   │   │   ├── CustomJwtDecoder.java
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/             # REST API Controllers
│   │   │   │   ├── ProfileController.java        (Public API)
│   │   │   │   └── InternalProfileController.java (Internal API)
│   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   │   ├── GetProfileRequest.java
│   │   │   │   │   └── ProfileCreationRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── PageResponse.java
│   │   │   │       └── UserProfileResponse.java
│   │   │   ├── entity/                 # Domain Entities
│   │   │   │   └── UserProfile.java
│   │   │   ├── exception/              # Exception Handling
│   │   │   │   ├── AppException.java
│   │   │   │   ├── ErrorCode.java
│   │   │   │   └── GlobalException.java
│   │   │   ├── mapper/                 # MapStruct Mappers
│   │   │   │   └── UserProfileMapper.java
│   │   │   ├── repository/             # Neo4j Repositories
│   │   │   │   └── UserProfileRepository.java
│   │   │   ├── service/                # Business Logic
│   │   │   │   └── UserProfileService.java
│   │   │   └── ProfileServiceApplication.java
│   │   └── resources/
│   │       └── application.yaml        # Application Configuration
│   └── test/                           # Unit & Integration Tests
├── target/                             # Compiled classes & generated code
├── pom.xml                             # Maven configuration
├── profile_openapi.yaml                # OpenAPI 3.0 Specification
└── README.md                           # This file
```

## 🛠️ Cài đặt và chạy

### Prerequisites
- Java 21 hoặc cao hơn
- Maven 3.6+
- Neo4j Database (chạy trên port 7687)

### 1. Clone repository
```bash
git clone <repository-url>
cd profile-service
```

### 2. Cài đặt Neo4j
```bash
# Sử dụng Docker (khuyến nghị)
docker run -d \
  --name neo4j \
  -p 7474:7474 -p 7687:7687 \
  -e NEO4J_AUTH=neo4j/12345678 \
  neo4j:latest

# Hoặc sử dụng docker-compose từ root project
docker-compose -f neo4j-compose.yml up -d
```

### 3. Cấu hình application
Chỉnh sửa `src/main/resources/application.yaml` nếu cần:
```yaml
spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: 12345678
```

### 4. Build & Run
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Hoặc chạy file JAR
java -jar target/profile-service-0.0.1-SNAPSHOT.jar
```

Service sẽ chạy tại: `http://localhost:8081/profile`

## 📚 API Documentation

### Public Endpoints

#### 1. Tìm profile theo User ID
```http
GET /profile/user/{userId}
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
  "code": 0,
  "message": "success!",
  "data": {
    "id": "abc123-def456",
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "username": "user123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "0123456789",
    "birthday": "1990-01-01",
    "identityNumber": "123456789012",
    "gender": true,
    "avatar": "http://localhost:8888/api/v1/file/media/download/xxx.jpg"
  }
}
```

#### 2. Tìm profile theo Username
```http
GET /profile/user/find/{username}
Authorization: Bearer <JWT_TOKEN>
```

### Internal Endpoints (Không cần authentication)

#### 1. Tạo profile mới
```http
POST /profile/internal/user
Content-Type: application/json

{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "username": "user123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "0123456789",
  "birthday": "1990-01-01",
  "identityNumber": "123456789012",
  "gender": true
}
```

#### 2. Lấy nhiều profiles
```http
POST /profile/internal/userId-list
Content-Type: application/json

{
  "userIdList": [
    "123e4567-e89b-12d3-a456-426614174000",
    "223e4567-e89b-12d3-a456-426614174001"
  ]
}
```

Chi tiết đầy đủ xem tại: `profile_openapi.yaml`

## ⚙️ Cấu hình

### Application Configuration
```yaml
server:
  port: 8081                              # Service port
  servlet:
    context-path: /profile                # Base path

spring:
  application:
    name: profile-service                 # Service name
  neo4j:
    uri: bolt://localhost:7687            # Neo4j connection
    authentication:
      username: neo4j
      password: 12345678

app:
  services:
    file:
      default-avatar: http://localhost:8888/api/v1/file/media/download/xxx.jpg
```

### Security Configuration
- **Public endpoints:** `/internal/**` (không cần authentication)
- **Protected endpoints:** `/user/**` (yêu cầu JWT token)
- **JWT Decoder:** Custom implementation với validation
- **Password Encoder:** BCrypt với strength 10

## 👨‍💻 Developer

**Dong Vu**  
Email: dongvu1105@example.com

---

## 📝 License

[Thêm thông tin license nếu cần]

---

**Note:** Service này là một phần của hệ thống VolunteerHub microservices và được thiết kế để hoạt động cùng với các services khác như Identity Service, File Service, Event Service, v.v.
