# Identity Service - Volunteer Hub

## 📋 Mô tả
Identity Service là một microservice chịu trách nhiệm quản lý xác thực, phân quyền và quản lý người dùng trong hệ thống Volunteer Hub. Service này cung cấp các chức năng đăng ký, đăng nhập, quản lý token JWT và phân quyền dựa trên vai trò (Role-Based Access Control).

## 🛠️ Công nghệ sử dụng

### Core Technologies
- **Java 21** - Phiên bản Java hiện đại
- **Spring Boot 3.5.7** - Framework chính
- **Spring Security** - Bảo mật và xác thực
- **Spring Data JPA** - Quản lý database
- **OAuth2 Resource Server** - Xác thực JWT

### Database & ORM
- **MySQL** - Cơ sở dữ liệu quan hệ
- **Hibernate** - ORM framework

### Security & Authentication
- **JWT (JSON Web Token)** - Access Token & Refresh Token
- **BCrypt** - Mã hóa mật khẩu
- **Nimbus JOSE JWT** - Xử lý JWT

### Code Quality & Productivity
- **Lombok** - Giảm boilerplate code
- **MapStruct** - Mapping giữa entities và DTOs
- **Spring Validation** - Kiểm tra dữ liệu đầu vào

### Microservices Communication
- **OpenFeign** - HTTP client để giao tiếp với Profile Service
- **Spring Cloud** - Hỗ trợ microservices architecture

## 🎯 Các chức năng chính

### 1. Quản lý Authentication (Xác thực)
- **Đăng ký (Register)**: `POST /identity/user/register`
  - Tạo tài khoản mới với email và mật khẩu
  - Tự động tạo profile thông qua Profile Service
  - Gán role mặc định cho người dùng

- **Đăng nhập (Login)**: `POST /identity/auth/login`
  - Xác thực bằng email và mật khẩu
  - Trả về Access Token và Refresh Token
  - Kiểm tra trạng thái tài khoản (bị khóa hay không)

- **Làm mới Token (Refresh Token)**: `POST /identity/auth/refresh-token`
  - Tạo cặp token mới từ refresh token
  - Tự động vô hiệu hóa refresh token cũ

- **Đăng xuất (Logout)**: `POST /identity/auth/logout`
  - Vô hiệu hóa access token và refresh token
  - Lưu token vào blacklist

- **Xác thực Token (Introspect)**: `POST /identity/auth/introspect`
  - Kiểm tra tính hợp lệ của access token

### 2. Quản lý User (Người dùng)
- **Tìm người dùng theo ID**: `GET /identity/user/find-by-id/{id}`
- **Tìm người dùng theo Email**: `GET /identity/user/find-by-email/{email}`
- **Lấy danh sách tất cả tài khoản**: `GET /identity/user/find-all-account`
  - Hỗ trợ phân trang (pagination)
  - Chỉ ADMIN mới có quyền truy cập

- **Tìm kiếm tài khoản**: `GET /identity/user/search?keyword={keyword}`
  - Tìm kiếm theo email
  - Hỗ trợ phân trang

### 3. Quản lý Vai trò (Role Management)
- **Lấy tất cả vai trò**: `GET /identity/user/role`
- **Khóa tài khoản**: `PUT /identity/user/lock/{userId}`
  - Chỉ ADMIN có quyền
- **Mở khóa tài khoản**: `PUT /identity/user/unlock/{userId}`
  - Chỉ ADMIN có quyền

## ✨ Điểm nổi bật

### 1. Bảo mật cao cấp
- **JWT với Access & Refresh Token**: Cơ chế 2 token riêng biệt
  - Access Token: Thời gian sống ngắn (1000s)
  - Refresh Token: Thời gian sống dài (30000s)
- **Token Invalidation**: Hệ thống blacklist token sau khi logout
- **BCrypt Password Encryption**: Mã hóa mật khẩu với strength 10
- **Custom JWT Decoder**: Xử lý và validate JWT linh hoạt

### 2. Phân quyền rõ ràng (RBAC)
- **Role-Based Access Control**: Phân quyền dựa trên vai trò
- **Method Security**: Sử dụng `@PreAuthorize` để kiểm soát quyền truy cập
- **Predefined Roles**: Vai trò được định nghĩa trước (ADMIN, USER, etc.)
- **JWT Scope**: Nhúng thông tin role vào token

### 3. Kiến trúc Microservices
- **Service Communication**: Tích hợp với Profile Service qua OpenFeign
- **Distributed Transaction Handling**: Xử lý rollback khi tạo profile thất bại
- **Context Path**: `/identity` - Dễ dàng routing qua API Gateway

### 4. Token Management thông minh
- **Token Type Validation**: Phân biệt access token và refresh token
- **Token Reuse Prevention**: Không cho phép sử dụng lại token đã logout
- **Automatic Token Cleanup**: Quản lý token hết hạn hiệu quả

### 5. Error Handling chuyên nghiệp
- **Custom Exception Handling**: ErrorCode và AppException
- **Global Exception Handler**: Xử lý exception tập trung
- **JWT Authentication Entry Point**: Xử lý lỗi xác thực JWT
- **Meaningful Error Messages**: Thông báo lỗi rõ ràng

### 6. Code Quality
- **MapStruct**: Mapping tự động giữa entities và DTOs
- **Lombok**: Giảm boilerplate code
- **Clean Architecture**: Phân tách rõ ràng layer (Controller-Service-Repository)
- **Validation**: Kiểm tra dữ liệu đầu vào với Bean Validation

### 7. Tính năng nâng cao
- **Pagination Support**: Hỗ trợ phân trang cho danh sách
- **Search Functionality**: Tìm kiếm tài khoản theo keyword
- **Account Status Management**: Quản lý trạng thái tài khoản (khóa/mở khóa)
- **API Response Wrapper**: Cấu trúc response thống nhất với ApiResponse

## 📂 Cấu trúc dự án

```
identity-service/
├── src/
│   ├── main/
│   │   ├── java/com/dongVu1105/identity_service/
│   │   │   ├── configuration/          # Security, JWT configuration
│   │   │   ├── constant/               # Constants (PredefinedRole)
│   │   │   ├── controller/             # REST Controllers
│   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   │   ├── request/            # Request DTOs
│   │   │   │   └── response/           # Response DTOs
│   │   │   ├── entity/                 # JPA Entities
│   │   │   ├── exception/              # Custom Exceptions
│   │   │   ├── mapper/                 # MapStruct Mappers
│   │   │   ├── repository/             # JPA Repositories
│   │   │   │   └── httpClient/         # Feign Clients
│   │   │   └── service/                # Business Logic
│   │   └── resources/
│   │       └── application.yaml        # Configuration
│   └── test/                           # Unit tests
└── pom.xml                             # Maven dependencies
```

## ⚙️ Cấu hình

### Database
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/volunteerHub-identity
    username: root
    password: 12345678
```

### JWT Settings
```yaml
jwt:
  signerKey: "your-secret-key"
  access-token-duration: 1000      # 16 minutes
  refresh-token-duration: 30000    # 8.3 hours
```

### Server
```yaml
server:
  port: 8080
  servlet:
    context-path: /identity
```

## 🚀 Cách chạy

### Prerequisites
- Java 21
- MySQL 8.0+
- Maven 3.8+

### Khởi động service
```bash
# Clone repository
git clone <repository-url>

# Di chuyển vào thư mục
cd identity-service

# Build project
mvn clean install

# Chạy application
mvn spring-boot:run
```

### Hoặc sử dụng Maven Wrapper
```bash
./mvnw spring-boot:run
```

## 📝 API Documentation
Chi tiết API được mô tả trong file `identity_openapi.yaml`

## 🔐 Security Flow

### 1. Registration Flow
```
User → Register → Identity Service → Create User → Profile Service → Return Response
```

### 2. Authentication Flow
```
User → Login → Validate Credentials → Generate JWT Tokens → Return Tokens
```

### 3. Token Refresh Flow
```
User → Refresh Token → Validate Refresh Token → Invalidate Old → Generate New Tokens
```

### 4. Logout Flow
```
User → Logout → Invalidate Access Token → Invalidate Refresh Token → Success
```

## 👥 Roles
- **ADMIN**: Quản trị viên - Có quyền quản lý tất cả tài khoản
- **USER**: Người dùng thông thường
- **Các role khác**: Được định nghĩa trong PredefinedRole

## 🔗 Integration
Service này tích hợp với:
- **Profile Service**: Quản lý thông tin cá nhân người dùng
- **API Gateway**: Routing và load balancing

## 📄 License
Developed by dongVu1105

---
**Version**: 0.0.1-SNAPSHOT  
**Spring Boot**: 3.5.7  
**Java**: 21
