# Event Service - Dịch vụ Quản lý Sự kiện Tình nguyện

## 📋 Mô tả
Event Service là một microservice trong hệ thống Volunteer Hub, chuyên quản lý toàn bộ các hoạt động liên quan đến sự kiện tình nguyện. Service này cung cấp các chức năng tạo, quản lý sự kiện và theo dõi người tham gia, hỗ trợ cho cả người quản lý sự kiện, tình nguyện viên và quản trị viên.

## 🚀 Công nghệ sử dụng

### Core Framework
- **Spring Boot 3.5.7** - Framework chính
- **Java 21** - Phiên bản Java hiện đại
- **Spring Cloud 2025.0.0** - Hỗ trợ microservices architecture

### Database & Messaging
- **MongoDB** - NoSQL database để lưu trữ dữ liệu sự kiện
- **Apache Kafka** - Message broker cho event-driven architecture
- **Spring Data MongoDB** - ORM cho MongoDB

### Security & Authentication
- **Spring Security** - Bảo mật ứng dụng
- **OAuth2 Resource Server** - Xác thực JWT token
- **Custom JWT Decoder** - Giải mã và validate JWT tokens

### Integration & Communication
- **OpenFeign** - HTTP client để giao tiếp với các microservices khác
  - Identity Service
  - File Service
  - Profile Service

### Development Tools
- **Lombok 1.18.30** - Giảm thiểu boilerplate code
- **MapStruct 1.5.5.Final** - Object mapping
- **Spring Validation** - Validate dữ liệu đầu vào
- **Maven** - Build tool và dependency management

## 🎯 Chức năng chính

### 1. Quản lý Sự kiện (Event Management)

#### Người quản lý sự kiện (Event Manager):
- ✅ **Tạo sự kiện mới** - Tạo sự kiện với thông tin chi tiết, hình ảnh, thời gian, địa điểm
- ✅ **Cập nhật sự kiện** - Chỉnh sửa thông tin sự kiện đã tạo
- ✅ **Xóa sự kiện** - Xóa sự kiện của chính mình
- ✅ **Xem danh sách người đăng ký** - Theo dõi người tham gia với phân trang
- ✅ **Duyệt đơn đăng ký** - Chấp nhận/từ chối đơn đăng ký tham gia
- ✅ **Xác nhận hoàn thành** - Xác nhận người tham gia đã hoàn thành sự kiện

#### Admin:
- 🔒 **Duyệt sự kiện** - Phê duyệt sự kiện trước khi công khai
- 🔒 **Xem sự kiện chờ duyệt** - Danh sách các sự kiện pending
- 🔒 **Xóa sự kiện** - Quyền xóa mọi sự kiện
- 🔒 **Nhận thông báo** - Nhận thông báo khi có sự kiện mới

### 2. Quản lý Người tham gia (Event User Management)

#### Người dùng/Tình nguyện viên:
- 📝 **Đăng ký sự kiện** - Đăng ký tham gia sự kiện yêu thích
- ❌ **Hủy đăng ký** - Hủy tham gia sự kiện
- 📊 **Xem sự kiện đã hoàn thành** - Lịch sử các sự kiện đã tham gia
- 🔍 **Lọc sự kiện** - Tìm kiếm theo danh mục và khoảng thời gian
- ✔️ **Kiểm tra trạng thái** - Kiểm tra đã đăng ký sự kiện chưa

#### Quản lý sự kiện:
- 👥 **Xem người đăng ký chờ duyệt** - Danh sách pending registrations
- ✅ **Duyệt đơn đăng ký** - Accept/reject registrations
- 📋 **Xem danh sách người tham gia** - Danh sách đang tham gia
- ✔️ **Xác nhận hoàn thành** - Đánh dấu người dùng đã hoàn thành sự kiện
- 📤 **Xuất danh sách** - Export danh sách tình nguyện viên ra file JSON

### 3. Quản lý Danh mục (Category Management)
- 📂 **Danh sách danh mục** - Lấy tất cả danh mục sự kiện (Từ thiện, Môi trường, Giáo dục, v.v.)

### 4. Tích hợp & Thông báo
- 🔔 **Kafka Messaging** - Gửi thông báo real-time:
  - Sự kiện mới tạo → Admin
  - Sự kiện được duyệt → Event Manager
  - Sự kiện bị từ chối → Event Manager
- 📁 **File Upload** - Upload và quản lý hình ảnh sự kiện
- 👤 **User Profile Integration** - Lấy thông tin người dùng từ Profile Service

## ✨ Điểm nổi bật

### 1. **Kiến trúc Microservices**
- Thiết kế độc lập, có thể scale riêng biệt
- Giao tiếp với các services khác qua OpenFeign
- Event-driven architecture với Kafka

### 2. **Bảo mật cao cấp**
- JWT-based authentication
- Role-based access control (RBAC)
- Method-level security với `@PreAuthorize`
- Custom JWT decoder cho validation nâng cao

### 3. **Workflow phê duyệt 2 cấp**
- Event Manager tạo sự kiện → Cần Admin duyệt
- User đăng ký sự kiện → Cần Event Manager duyệt
- Đảm bảo chất lượng và kiểm soát nội dung

### 4. **Real-time Notifications**
- Sử dụng Kafka để gửi thông báo tức thời
- Event-driven messaging cho các hành động quan trọng
- Topics: `new-event`, `accept-event`, `reject-event`

### 5. **Quản lý File thông minh**
- Upload hình ảnh cho sự kiện
- Tích hợp với File Service
- Hỗ trợ multipart/form-data

### 6. **Validation & Error Handling**
- Custom validators:
  - `StartDateValidator` - Validate ngày bắt đầu
  - `FinishDateValidator` - Validate ngày kết thúc
- Global exception handling
- Structured error responses với `ErrorCode`

### 7. **Pagination & Filtering**
- Phân trang cho tất cả danh sách
- Lọc theo danh mục và khoảng thời gian
- Sort theo ngày tạo (mới nhất trước)

### 8. **Data Export**
- Export danh sách tình nguyện viên ra JSON
- Hỗ trợ báo cáo và phân tích

### 9. **Clean Architecture**
- Separation of concerns rõ ràng
- DTO pattern cho request/response
- MapStruct cho object mapping tự động
- Repository pattern cho data access

### 10. **MongoDB NoSQL**
- Linh hoạt với schema
- Auto-indexing
- Phù hợp cho dữ liệu phi cấu trúc

## 📁 Cấu trúc dự án

```
event-service/
├── src/main/java/com/dongVu1105/event_service/
│   ├── configuration/          # Cấu hình Security, Feign, JWT
│   ├── controller/             # REST API endpoints
│   │   ├── EventController.java
│   │   ├── EventUserController.java
│   │   └── CategoryController.java
│   ├── dto/                    # Data Transfer Objects
│   │   ├── request/           # Request DTOs
│   │   └── response/          # Response DTOs
│   ├── entity/                # MongoDB entities
│   │   ├── Event.java
│   │   ├── EventUser.java
│   │   └── Category.java
│   ├── enums/                 # Enumerations
│   ├── exception/             # Exception handling
│   ├── mapper/                # MapStruct mappers
│   ├── repository/            # MongoDB repositories & Feign clients
│   ├── service/               # Business logic
│   └── validation/            # Custom validators
├── src/main/resources/
│   └── application.yaml       # Cấu hình ứng dụng
└── pom.xml                    # Maven dependencies
```

## 🔧 Cấu hình

### Application Configuration
```yaml
server:
  port: 8084
  servlet:
    context-path: /event

spring:
  data:
    mongodb:
      uri: mongodb://root:root@localhost:27017/event-service?authSource=admin
  kafka:
    bootstrap-servers: localhost:9094
```

### Dependencies với các services khác
- **Identity Service** (port 8080) - Xác thực và quản lý users
- **File Service** (port 8082) - Upload và quản lý files
- **Profile Service** (port 8081) - Thông tin profile người dùng

## 🚦 API Endpoints

### Event Management
- `POST /event/create` - Tạo sự kiện mới
- `PUT /event/update` - Cập nhật sự kiện
- `GET /event/{eventId}` - Chi tiết sự kiện
- `GET /event/category-date` - Lọc sự kiện theo danh mục và ngày
- `GET /event/status/{eventId}` - Kiểm tra trạng thái sự kiện
- `GET /event/pending` - Danh sách sự kiện chờ duyệt (Admin)
- `PUT /event/accept/{eventId}` - Duyệt sự kiện (Admin)
- `DELETE /event/delete/{eventId}` - Xóa sự kiện

### Event User Management
- `POST /event/user/register` - Đăng ký sự kiện
- `DELETE /event/user/unsubscribe/{eventId}` - Hủy đăng ký
- `GET /event/user/completed-event` - Sự kiện đã hoàn thành
- `GET /event/user/pending` - Người đăng ký chờ duyệt
- `PUT /event/user/accept-registration/{eventUserId}` - Duyệt đăng ký
- `DELETE /event/user/reject-registration/{eventUserId}` - Từ chối đăng ký
- `PUT /event/user/confirm-completion/{eventUserId}` - Xác nhận hoàn thành
- `GET /event/user/attending` - Người đang tham gia
- `GET /event/user/find-all` - Tất cả người trong sự kiện
- `GET /event/user/exist/{userId}/{eventId}` - Kiểm tra user trong sự kiện
- `GET /event/user/export/file` - Xuất danh sách

### Category Management
- `GET /event/categories` - Danh sách danh mục

## 👥 Roles & Permissions

- **ADMIN**: Quản trị viên - Duyệt/xóa sự kiện, xem tất cả
- **EVENT_MANAGER**: Quản lý sự kiện - Tạo/sửa/xóa sự kiện, duyệt người tham gia
- **USER**: Người dùng - Xem và đăng ký sự kiện

## 🔄 Event Flow

1. **Tạo sự kiện**: Event Manager tạo → Gửi thông báo đến Admin
2. **Phê duyệt**: Admin duyệt → Sự kiện công khai → Thông báo Event Manager
3. **Đăng ký**: User đăng ký → Chờ Event Manager duyệt
4. **Tham gia**: Event Manager duyệt → User tham gia sự kiện
5. **Hoàn thành**: Event Manager xác nhận hoàn thành → Cập nhật lịch sử

## 🛠️ Build & Run

```bash
# Build project
./mvnw clean install

# Run service
./mvnw spring-boot:run

# Hoặc chạy với Docker
docker-compose up
```

## 📝 License
Dự án thuộc về dongVu1105

---
**Version**: 0.0.1-SNAPSHOT  
**Java**: 21  
**Spring Boot**: 3.5.7
