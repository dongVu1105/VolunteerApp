# Post Service - Volunteer Hub

## 📋 Mô tả
Post Service là một microservice trong hệ thống Volunteer Hub, chịu trách nhiệm quản lý tất cả các hoạt động liên quan đến bài viết, bình luận và tương tác trong các sự kiện tình nguyện. Service này cung cấp nền tảng để người dùng có thể chia sẻ, thảo luận và tương tác trong các sự kiện mà họ tham gia.

## 🚀 Công nghệ sử dụng

### Core Technologies
- **Java 21** - Ngôn ngữ lập trình chính
- **Spring Boot 3.5.7** - Framework chính cho backend
- **Spring Cloud 2025.0.0** - Microservices framework
- **MongoDB** - Cơ sở dữ liệu NoSQL để lưu trữ posts, comments, reactions

### Key Dependencies
- **Spring Web** - REST API development
- **Spring Security OAuth2 Resource Server** - Bảo mật và xác thực JWT
- **Spring Data MongoDB** - Tương tác với MongoDB
- **Spring Cloud OpenFeign** - HTTP client để giao tiếp với các microservices khác
- **Spring Kafka** - Message broker để gửi thông báo realtime
- **Lombok** - Giảm boilerplate code
- **MapStruct 1.5.5** - Object mapping

## ✨ Các chức năng chính

### 1. Quản lý Bài viết (Posts)
- **Tạo bài viết**: Người dùng có thể tạo bài viết mới trong sự kiện với text và hình ảnh
- **Xem bài viết**: Lấy thông tin chi tiết bài viết theo ID
- **Xóa bài viết**: Chủ bài viết hoặc quản lý sự kiện có quyền xóa
- **Danh sách bài viết**: Phân trang và sắp xếp theo thời gian (mới nhất trước)
- **Upload hình ảnh**: Hỗ trợ upload file đính kèm

### 2. Quản lý Bình luận (Comments)
- **Bình luận bài viết**: Thêm comment vào bài viết với text và hình ảnh
- **Xem danh sách comment**: Lấy tất cả comments của một bài viết (có phân trang)
- **Xóa comment**: Người tạo có thể xóa comment của mình
- **Đếm số lượng comment**: Thống kê số comment cho mỗi bài viết

### 3. Quản lý Phản hồi Bình luận (Child Comments)
- **Trả lời comment**: Reply vào một comment cụ thể
- **Xem replies**: Lấy tất cả các reply của một comment (có phân trang)
- **Xóa reply**: Người tạo có thể xóa reply của mình

### 4. Quản lý Reactions
- **Like bài viết**: Thích/bỏ thích bài viết
- **Like comment**: Thích/bỏ thích comment (thông qua ChildReact)
- **Xem danh sách reactions**: Lấy danh sách người đã like (có phân trang)
- **Đếm số lượng**: Thống kê số lượng reactions
- **Trạng thái reaction**: Kiểm tra người dùng hiện tại đã react hay chưa

## 🎯 Điểm nổi bật

### 1. Kiến trúc Microservices
- **Độc lập và mở rộng**: Service hoạt động độc lập, dễ dàng scale theo nhu cầu
- **Service Communication**: Sử dụng OpenFeign để giao tiếp đồng bộ với:
  - `identity-service`: Xác thực và ủy quyền
  - `event-service`: Xác minh sự kiện và quyền truy cập
  - `file-service`: Upload và quản lý file
  - `profile-service`: Lấy thông tin người dùng

### 2. Bảo mật Toàn diện
- **JWT Authentication**: Xác thực mọi request qua OAuth2 Resource Server
- **Custom JWT Decoder**: Giải mã và validate JWT token
- **Authorization**: Kiểm tra quyền truy cập dựa trên:
  - Chủ sở hữu bài viết/comment
  - Quản lý sự kiện
  - Thành viên sự kiện

### 3. Realtime Notifications
- **Kafka Integration**: Gửi thông báo realtime khi có bài viết mới
- **Event-Driven**: Sử dụng message queue để notify quản lý sự kiện
- **Asynchronous**: Không blocking request chính khi gửi notification

### 4. NoSQL Database Design
- **MongoDB**: Sử dụng document-based database phù hợp với dữ liệu động
- **Auto-indexing**: Tự động tạo index để tối ưu hiệu suất
- **Flexible Schema**: Dễ dàng mở rộng cấu trúc dữ liệu

### 5. Rich Media Support
- **File Upload**: Hỗ trợ upload hình ảnh cho posts và comments
- **MultipartFile**: Xử lý file upload thông qua Spring
- **Integration với File Service**: Centralized file management

### 6. Pagination & Sorting
- **Spring Data Pagination**: Phân trang hiệu quả cho tất cả list endpoints
- **Custom Sort**: Sắp xếp theo thời gian (mới nhất trước)
- **PageResponse**: Response model chuẩn với metadata đầy đủ

### 7. Data Mapping
- **MapStruct**: Auto-generate mapping code giữa Entity và DTO
- **Type-safe**: Kiểm tra lỗi tại compile-time
- **Performance**: Mapping nhanh hơn reflection-based solutions

### 8. Exception Handling
- **Global Exception Handler**: Xử lý exception tập trung
- **Custom Error Codes**: Định nghĩa rõ ràng các lỗi nghiệp vụ
- **Consistent Response**: API response format nhất quán

## 📁 Cấu trúc dự án

```
post-service/
├── src/main/java/com/dongVu1105/post_service/
│   ├── configuration/          # Security, Feign, JWT configs
│   ├── controller/            # REST API endpoints
│   │   ├── PostController
│   │   ├── CommentController
│   │   ├── ChildCommentController
│   │   ├── ReactController
│   │   └── ChildReactController
│   ├── dto/                   # Data Transfer Objects
│   │   ├── request/
│   │   └── response/
│   ├── entity/                # MongoDB entities
│   │   ├── Post
│   │   ├── Comment
│   │   ├── ChildComment
│   │   ├── React
│   │   └── ChildReact
│   ├── exception/             # Custom exceptions
│   ├── mapper/                # MapStruct mappers
│   ├── repository/            # MongoDB repositories
│   │   └── httpClient/        # Feign clients
│   └── service/               # Business logic
└── src/main/resources/
    └── application.yaml       # Configuration
```

## 🔧 Cấu hình

### Application Properties
```yaml
Server Port: 8085
Context Path: /post
Database: MongoDB (localhost:27017)
Kafka: localhost:9094
```

### Dependencies với Services khác
- **identity-service**: http://localhost:8080/identity
- **event-service**: http://localhost:8084/event
- **file-service**: http://localhost:8082/file
- **profile-service**: http://localhost:8081/profile

## 🏃 Hướng dẫn chạy

### Yêu cầu
- Java 21 hoặc cao hơn
- MongoDB đang chạy tại localhost:27017
- Kafka đang chạy tại localhost:9094
- Các microservices liên quan (identity, event, file, profile) đang hoạt động

### Build và chạy
```bash
# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

Service sẽ khởi động tại: `http://localhost:8085/post`

## 📊 Database Schema

### Collections
- **post**: Lưu trữ bài viết
- **comment**: Lưu trữ bình luận chính
- **child-comment**: Lưu trữ replies
- **react**: Lưu trữ reactions cho posts
- **child-react**: Lưu trữ reactions cho comments

## 🔐 Authentication
Tất cả endpoints (trừ PUBLIC_ENDPOINTS) yêu cầu JWT token trong header:
```
Authorization: Bearer <jwt-token>
```

## 📝 API Documentation
Chi tiết API có sẵn tại: `post_openapi.yaml`

## 👨‍💻 Tác giả
dongVu1105

## Giới thiệu
Post Service là một microservice trong hệ thống Volunteer Hub, chịu trách nhiệm quản lý các bài viết (posts), bình luận (comments) và tương tác (reactions) trong các sự kiện tình nguyện. Service này cho phép người dùng tham gia sự kiện chia sẻ nội dung, tương tác với nhau thông qua bài viết và bình luận.

## Công nghệ sử dụng

### Core Framework
- **Spring Boot 3.5.7** - Framework chính để xây dựng microservice
- **Java 21** - Phiên bản Java mới nhất với các tính năng hiện đại

### Cơ sở dữ liệu
- **MongoDB** - NoSQL database cho việc lưu trữ dữ liệu posts, comments, reactions với khả năng mở rộng cao

### Security
- **Spring Security OAuth2 Resource Server** - Xác thực và phân quyền dựa trên JWT token
- **Custom JWT Decoder** - Giải mã và xác thực JWT token tùy chỉnh

### Communication
- **Spring Cloud OpenFeign** - HTTP client để giao tiếp với các microservices khác:
  - Identity Service: Xác thực người dùng
  - Event Service: Quản lý sự kiện
  - File Service: Quản lý file/hình ảnh
  - Profile Service: Thông tin người dùng
- **Apache Kafka** - Message broker cho việc gửi thông báo bất đồng bộ

### Utilities
- **Lombok** - Giảm boilerplate code
- **MapStruct 1.5.5** - Object mapping tự động giữa entities và DTOs
- **Maven** - Build tool và dependency management

## Các chức năng chính

### 1. Quản lý Bài viết (Posts)
- **Tạo bài viết**: Người dùng tham gia sự kiện có thể tạo bài viết với văn bản và hình ảnh
- **Xem bài viết**: Lấy danh sách bài viết theo sự kiện với phân trang
- **Xem chi tiết bài viết**: Xem thông tin chi tiết một bài viết
- **Xóa bài viết**: Chủ bài viết hoặc quản lý sự kiện có thể xóa bài viết

### 2. Quản lý Bình luận (Comments)
- **Bình luận trên bài viết**: Thêm bình luận với văn bản hoặc hình ảnh
- **Bình luận lồng nhau (Child Comments)**: Trả lời các bình luận khác
- **Xem danh sách bình luận**: Lấy tất cả bình luận của một bài viết với phân trang
- **Xóa bình luận**: Chủ bình luận hoặc quản lý sự kiện có thể xóa

### 3. Tương tác (Reactions)
- **React bài viết**: Like/Unlike bài viết
- **React bình luận**: Like/Unlike bình luận
- **Đếm reactions**: Hiển thị số lượng reactions trên posts và comments
- **Trạng thái reaction**: Kiểm tra người dùng hiện tại đã react hay chưa

### 4. Thông báo (Notifications)
- **Kafka Integration**: Gửi thông báo real-time khi có bài viết mới qua Kafka topic "post"
- **Thông báo cho quản lý**: Tự động thông báo cho người quản lý sự kiện khi có bài viết mới

## Điểm nổi bật

### 1. Kiến trúc Microservices
- **Độc lập và mở rộng**: Service hoạt động độc lập, dễ dàng scale theo nhu cầu
- **Giao tiếp hiệu quả**: Sử dụng Feign Client cho synchronous communication và Kafka cho asynchronous messaging

### 2. Bảo mật cao
- **JWT Authentication**: Mọi endpoint đều được bảo vệ bằng JWT token
- **Custom JWT Decoder**: Xử lý và xác thực JWT một cách linh hoạt
- **Authorization**: Kiểm soát quyền truy cập dựa trên vai trò người dùng và quyền sở hữu

### 3. Xác thực nghiệp vụ thông minh
- **Kiểm tra tư cách thành viên**: Chỉ người tham gia sự kiện mới được đăng bài
- **Kiểm tra trạng thái sự kiện**: Chỉ cho phép tương tác với sự kiện đang hoạt động
- **Phân quyền linh hoạt**: Chủ sở hữu và quản lý sự kiện có quyền quản lý nội dung

### 4. Tối ưu hiệu năng
- **MongoDB**: Sử dụng NoSQL cho việc truy vấn nhanh và dữ liệu phi cấu trúc
- **Phân trang**: Hỗ trợ phân trang cho tất cả danh sách lớn
- **Indexing**: Tự động tạo index cho MongoDB để tăng tốc truy vấn

### 5. Trải nghiệm người dùng
- **Upload media**: Tích hợp với File Service để upload và quản lý hình ảnh
- **Thông tin phong phú**: Hiển thị avatar, username, thời gian, số lượng reactions và comments
- **Realtime updates**: Thông báo thời gian thực qua Kafka

### 6. Thiết kế code chất lượng
- **Clean Architecture**: Phân tách rõ ràng các layer (Controller, Service, Repository, Entity)
- **DTO Pattern**: Sử dụng Request/Response DTOs để tách biệt internal entities
- **MapStruct**: Mapping tự động giữa entities và DTOs, giảm thiểu lỗi
- **Exception Handling**: Xử lý lỗi tập trung với Global Exception Handler

## Cấu trúc dự án

```
post-service/
├── configuration/          # Cấu hình Security, JWT, Feign
├── controller/            # REST API endpoints
├── dto/                   # Data Transfer Objects
│   ├── request/          # Request DTOs
│   └── response/         # Response DTOs
├── entity/               # MongoDB entities
├── exception/            # Custom exceptions và error handling
├── mapper/               # MapStruct mappers
├── repository/           # MongoDB repositories và Feign clients
└── service/              # Business logic
```

## API Endpoints

### Posts
- `POST /post/create` - Tạo bài viết mới
- `GET /post/{id}` - Xem chi tiết bài viết
- `GET /post/find-all` - Lấy danh sách bài viết theo sự kiện
- `DELETE /post/delete/{postId}` - Xóa bài viết

### Comments
- `POST /post/comment/create` - Tạo bình luận
- `GET /post/comment/find-all` - Lấy danh sách bình luận
- `DELETE /post/comment/delete/{commentId}` - Xóa bình luận

### Child Comments
- `POST /post/child-comment/create` - Tạo bình luận trả lời
- `GET /post/child-comment/find-all` - Lấy danh sách bình luận trả lời
- `DELETE /post/child-comment/delete/{childCommentId}` - Xóa bình luận trả lời

### Reactions
- `POST /post/react/create` - React bài viết
- `POST /post/child-react/create` - React bình luận

## Cấu hình

### Application Properties
```yaml
server:
  port: 8085
  context-path: /post

spring:
  data:
    mongodb:
      uri: mongodb://root:root@localhost:27017/event-service
  kafka:
    bootstrap-servers: localhost:9094
```

### Dependencies Services
- Identity Service: `http://localhost:8080/identity`
- Event Service: `http://localhost:8084/event`
- File Service: `http://localhost:8082/file`
- Profile Service: `http://localhost:8081/profile`

## Yêu cầu hệ thống
- Java 21 hoặc cao hơn
- MongoDB 4.0+
- Apache Kafka 2.8+
- Maven 3.6+

## Cài đặt và chạy

1. **Clone repository**
   ```bash
   git clone <repository-url>
   cd post-service
   ```

2. **Cấu hình MongoDB và Kafka**
   - Đảm bảo MongoDB đang chạy tại `localhost:27017`
   - Đảm bảo Kafka đang chạy tại `localhost:9094`

3. **Build project**
   ```bash
   ./mvnw clean install
   ```

4. **Chạy service**
   ```bash
   ./mvnw spring-boot:run
   ```

Service sẽ chạy tại `http://localhost:8085/post`

## Tác giả
dongVu1105

## License
Demo project for Spring Boot
