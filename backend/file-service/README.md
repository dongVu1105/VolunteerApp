# File Service

Service quản lý file và media cho hệ thống VolunteerHub, cung cấp các chức năng upload và download file một cách an toàn và hiệu quả.

## 🚀 Công nghệ sử dụng

### Core Technologies
- **Java 21** - Ngôn ngữ lập trình chính
- **Spring Boot 3.5.7** - Framework chính cho ứng dụng
- **Spring Web** - Xây dựng RESTful API
- **Spring Data MongoDB** - Lưu trữ metadata của file
- **Spring Security OAuth2 Resource Server** - Bảo mật API với JWT

### Supporting Libraries
- **Lombok 1.18.30** - Giảm boilerplate code
- **MapStruct 1.5.5** - Object mapping
- **Spring Cloud OpenFeign** - Inter-service communication
- **Maven** - Build tool và dependency management

### Database
- **MongoDB** - Lưu trữ thông tin metadata của file (tên file, user, checksum, content-type, size, path)

### Storage
- **File System** - Lưu trữ file vật lý trên disk với UUID naming

## 📋 Các chức năng chính

### 1. Upload File
- **Endpoint**: `POST /internal/media/upload`
- **Bảo mật**: Yêu cầu JWT authentication
- **Chức năng**:
  - Upload file lên server
  - Tự động tạo UUID cho mỗi file
  - Tính toán MD5 checksum để đảm bảo tính toàn vẹn
  - Lưu metadata vào MongoDB
  - Lưu file vật lý vào hệ thống file
  - Trả về URL để download file

### 2. Download File
- **Endpoint**: `GET /media/download/{fileName}`
- **Bảo mật**: Public endpoint (không yêu cầu authentication)
- **Chức năng**:
  - Tải xuống file dựa trên UUID
  - Tự động set Content-Type phù hợp
  - Hỗ trợ nhiều loại file (image, PDF, binary, v.v.)

## ✨ Điểm nổi bật

### 1. **Bảo mật đa lớp**
- ✅ JWT authentication cho upload endpoint
- ✅ Public access cho download endpoint (thuận tiện cho việc hiển thị ảnh)
- ✅ Custom JWT Decoder với validation chặt chẽ
- ✅ OAuth2 Resource Server pattern

### 2. **Quản lý file thông minh**
- ✅ **UUID naming**: Tránh trùng lặp tên file và conflict
- ✅ **MD5 Checksum**: Đảm bảo tính toàn vẹn của file
- ✅ **Metadata tracking**: Lưu thông tin chi tiết (userId, size, contentType, path)
- ✅ **Extension preservation**: Giữ nguyên extension của file gốc

### 3. **Kiến trúc tách biệt**
- ✅ **Dual storage**: 
  - File vật lý trên file system
  - Metadata trên MongoDB
- ✅ **Internal vs Public endpoints**: Tách biệt upload (internal) và download (public)
- ✅ **Microservice ready**: Tích hợp sẵn với API Gateway và các service khác

### 4. **Khả năng mở rộng**
- ✅ **MongoDB**: NoSQL database giúp scale dễ dàng
- ✅ **Stateless design**: Dễ dàng deploy multiple instances
- ✅ **File storage configuration**: Dễ dàng chuyển đổi storage backend (local → cloud storage)
- ✅ **OpenFeign integration**: Sẵn sàng giao tiếp với các service khác

### 4. **Error Handling**
- ✅ Global exception handler
- ✅ Custom error codes và messages
- ✅ Detailed error responses cho client

### 5. **Developer Experience**
- ✅ **MapStruct**: Type-safe object mapping
- ✅ **Lombok**: Clean code với ít boilerplate
- ✅ **OpenAPI documentation**: Tài liệu API chi tiết
- ✅ **DTO pattern**: Tách biệt request/response models

## 🏗️ Cấu trúc dự án

```
file-service/
├── configuration/          # Security & JWT configuration
│   ├── CustomJwtDecoder.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── SecurityConfig.java
├── controller/            # REST controllers
│   ├── FileController.java          # Public endpoints
│   └── InternalFileController.java  # Internal endpoints
├── dto/                   # Data Transfer Objects
│   ├── request/
│   │   └── FileInfo.java
│   └── response/
│       ├── FileData.java
│       └── FileResponse.java
├── entity/               # MongoDB entities
│   └── FileManagement.java
├── exception/            # Custom exceptions
│   ├── AppException.java
│   ├── ErrorCode.java
│   └── GlobalException.java
├── mapper/               # MapStruct mappers
│   └── FileManagementMapper.java
├── repository/           # Data access layer
│   ├── FileManagementRepository.java  # MongoDB repository
│   └── FileRepository.java            # File system repository
└── service/              # Business logic
    └── FileService.java
```

## ⚙️ Cấu hình

### Application Configuration (application.yaml)
```yaml
server:
  port: 8082
  servlet:
    context-path: /file

spring:
  data:
    mongodb:
      uri: mongodb://root:root@localhost:27017/file-service?authSource=admin

app:
  file:
    storage-dir: "backend/file-service/file-storage"
    download-prefix: http://localhost:8888/api/v1/file/media/download/
```

## 🔌 API Endpoints

### Public Endpoints
- `GET /media/download/{fileName}` - Tải xuống file

### Internal Endpoints (Require Authentication)
- `POST /internal/media/upload` - Upload file (multipart/form-data)

## 📦 Dependencies chính

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    
    <!-- Spring Cloud -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    
    <!-- Utilities -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
    </dependency>
</dependencies>
```

## 🚦 Cách chạy service

### Prerequisites
- Java 21+
- MongoDB running on localhost:27017
- Maven 3.6+

### Steps
1. Clone repository
2. Đảm bảo MongoDB đang chạy
3. Build project:
   ```bash
   mvn clean install
   ```
4. Run application:
   ```bash
   mvn spring-boot:run
   ```
5. Service sẽ chạy tại: `http://localhost:8082/file`

## 🔐 Security Model

- **Upload**: Yêu cầu JWT token hợp lệ, service tự động lấy userId từ token để tracking
- **Download**: Public access, chỉ cần biết UUID của file
- **JWT Validation**: Custom decoder với validation logic riêng
- **CSRF**: Disabled (phù hợp cho API service)

## 📊 Data Flow

### Upload Flow
1. Client gửi file với JWT token → Internal API
2. Service validate token và extract userId
3. File được lưu với UUID name vào file system
4. Metadata (userId, checksum, size, contentType, path) lưu vào MongoDB
5. Return URL download cho client

### Download Flow
1. Client request với fileName (UUID)
2. Service query MongoDB để lấy metadata
3. Đọc file từ file system
4. Return file với đúng Content-Type

## 🎯 Use Cases

- Upload và quản lý avatar của users
- Upload ảnh/file đính kèm trong events
- Upload tài liệu trong posts
- Chia sẻ file giữa các services trong hệ thống microservices

## 📝 Notes

- File được lưu với UUID để tránh conflict và bảo mật
- Thư mục `file-storage/` chứa các file thực tế
- MongoDB chỉ lưu metadata, không lưu binary data
- Download endpoint là public để dễ dàng embed ảnh trong web/mobile app
