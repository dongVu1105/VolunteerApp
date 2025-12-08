# Notification Service

## 📋 Mô tả
Notification Service là một microservice trong hệ thống Volunteer Hub, chuyên quản lý và gửi thông báo real-time đến người dùng. Service này sử dụng kết hợp Socket.IO và Apache Kafka để đảm bảo việc gửi thông báo nhanh chóng, đáng tin cậy và mở rộng tốt.

## 🛠️ Công nghệ sử dụng

### Core Technologies
- **Java 21** - Ngôn ngữ lập trình chính
- **Spring Boot 3.5.7** - Framework backend
- **Spring Cloud 2025.0.0** - Microservices infrastructure

### Database & Messaging
- **MongoDB** - Cơ sở dữ liệu NoSQL để lưu trữ thông báo
- **Apache Kafka** - Message broker cho event-driven architecture
- **Netty Socket.IO 2.0.13** - WebSocket server cho real-time communication

### Security & Authentication
- **Spring Security OAuth2 Resource Server** - Bảo mật và xác thực JWT
- **Custom JWT Decoder** - Xử lý token tùy chỉnh

### Integration & Communication
- **OpenFeign** - HTTP client để gọi các service khác
- **Spring Kafka** - Kafka integration cho Spring

### Development Tools
- **Lombok 1.18.30** - Giảm boilerplate code
- **MapStruct 1.5.5** - Object mapping
- **Spring Validation** - Validation dữ liệu

## ⚙️ Cấu hình

### Application Configuration
- **Port**: 8083
- **Context Path**: `/notification`
- **Socket.IO Port**: 8099

### MongoDB
```yaml
URI: mongodb://root:root@localhost:27017/notification-service?authSource=admin
Auto-index: Enabled
```

### Kafka
- **Bootstrap Server**: localhost:9094
- **Consumer Group**: notification-group
- **Auto Offset Reset**: earliest

### Service Dependencies
- Identity Service: http://localhost:8080/identity
- Event Service: http://localhost:8084/event
- Profile Service: http://localhost:8081/profile

## 🌟 Các chức năng chính

### 1. Real-time Notifications
- Gửi thông báo thời gian thực qua WebSocket (Socket.IO)
- Quản lý các session WebSocket của người dùng
- Hỗ trợ nhiều loại thông báo khác nhau

### 2. Event-driven Architecture
Service lắng nghe các Kafka topics sau:

#### Event Management
- **new-event**: Thông báo sự kiện mới cần phê duyệt (gửi đến Admin)
- **accept-event**: Thông báo sự kiện được duyệt (gửi đến Event Manager)
- **reject-event**: Thông báo sự kiện bị từ chối (gửi đến Event Manager)

#### Event Registration
- **register-event**: Thông báo đăng ký mới (gửi đến Event Manager)
- **accept-register**: Thông báo đăng ký được chấp nhận (gửi đến User)
- **reject-register**: Thông báo đăng ký bị từ chối (gửi đến User)
- **confirm-completion**: Xác nhận hoàn thành sự kiện (gửi đến User)

#### Social Features
- **post**: Thông báo bài viết mới
- **comment**: Thông báo bình luận mới
- **reply-comment**: Thông báo trả lời bình luận
- **react**: Thông báo lượt thích bài viết
- **react-of-comment**: Thông báo lượt thích bình luận

### 3. Notification Management
- **Lấy danh sách thông báo**: Phân trang, sắp xếp theo thời gian
- **Đánh dấu đã đọc**: Cập nhật trạng thái thông báo
- **Lọc theo người nhận**: Tự động lọc thông báo theo user đang đăng nhập

### 4. Multi-role Notification Support
Service hỗ trợ gửi thông báo đến các vai trò khác nhau:
- **User** - Người dùng thông thường
- **Event Manager** - Người quản lý sự kiện
- **Admin** - Quản trị viên hệ thống

## 🎯 Điểm nổi bật

### 1. **Hybrid Notification System**
Kết hợp Kafka (message queue) và Socket.IO (WebSocket) để tạo hệ thống thông báo mạnh mẽ:
- **Kafka**: Đảm bảo message không bị mất, hỗ trợ retry và scalability
- **Socket.IO**: Gửi thông báo real-time với độ trễ thấp

### 2. **Generic Notification Entity**
Sử dụng Java Generics (`Notification<T>`) cho phép lưu trữ đa dạng loại thông tin:
```java
Notification<EventInfo>      // Thông báo về sự kiện
Notification<EventUserInfo>  // Thông báo về người tham gia
Notification<EventResponse>  // Thông báo phản hồi sự kiện
Notification<PostNoti>       // Thông báo về bài viết/tương tác
```

### 3. **Predefined Notification Templates**
Tất cả thông báo được định nghĩa trước với subject và content chuẩn hóa, đảm bảo tính nhất quán:
- Dễ dàng quản lý và cập nhật nội dung thông báo
- Hỗ trợ đa ngôn ngữ trong tương lai
- Tránh hard-code message trong code

### 4. **Smart Session Management**
- Tự động tracking WebSocket sessions của người dùng
- Chỉ gửi thông báo đến những người đang online
- Lưu trữ thông báo vào database cho người offline

### 5. **Role-based Notification Routing**
Thông báo được gửi đến đúng event channel theo vai trò:
- `user-noti` - Channel cho user
- `event-manager-noti` - Channel cho event manager
- `admin-noti` - Channel cho admin
- `post` - Channel cho social interactions

### 6. **Security First**
- JWT authentication cho REST API
- Custom JWT decoder
- OAuth2 Resource Server protection
- Context-aware notification filtering (chỉ lấy thông báo của user hiện tại)

### 7. **Scalable Architecture**
- Stateless design
- Kafka consumer group support
- MongoDB sharding ready
- Horizontal scaling capability

### 8. **Developer Friendly**
- MapStruct cho object mapping tự động
- Lombok giảm boilerplate code
- OpenAPI documentation support
- Comprehensive error handling với custom ErrorCode

## 📁 Cấu trúc dự án

```
notification-service/
├── configuration/          # Security, JWT, Socket.IO config
├── constant/              # PredefinedNotification templates
├── controller/            # REST controllers & Kafka listeners
├── dto/                   # Data Transfer Objects
│   ├── request/          # Request DTOs
│   └── response/         # Response DTOs
├── entity/               # MongoDB entities
├── exception/            # Exception handling
├── mapper/               # MapStruct mappers
├── repository/           # MongoDB repositories
│   └── httpClient/       # Feign clients
└── service/              # Business logic
```

## 🚀 Chạy ứng dụng

### Prerequisites
- Java 21
- Maven 3.9+
- MongoDB
- Apache Kafka
- Các service dependencies đang chạy (identity, event, profile)

### Build & Run
```bash
# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

### Testing Socket.IO
Connect to WebSocket server:
```javascript
const socket = io('http://localhost:8099');

socket.on('user-noti', (data) => {
    console.log('Received notification:', data);
});
```

## 📊 API Endpoints

### REST API
- `GET /notification/find-all` - Lấy danh sách thông báo (với phân trang)
  - Query params: `page` (default: 1), `size` (default: 10)
- `PUT /notification/{notificationId}` - Đánh dấu thông báo đã đọc

### WebSocket Events
- **Listen Events**: `user-noti`, `event-manager-noti`, `admin-noti`, `post`

## 🔄 Message Flow

1. **Event occurs** trong một service khác (event-service, post-service, etc.)
2. **Service publish** message lên Kafka topic tương ứng
3. **Notification Service consume** message từ Kafka
4. **Lưu notification** vào MongoDB
5. **Kiểm tra WebSocket session** của người nhận
6. **Gửi real-time notification** qua Socket.IO (nếu user online)
7. **User nhận thông báo** ngay lập tức hoặc xem sau khi login

## 📝 Notes
- Service này là event consumer, không publish Kafka messages
- Mọi thông báo đều được persist vào MongoDB trước khi gửi
- WebSocket connection requires valid JWT token
- Notification retention policy có thể cấu hình theo nhu cầu

---

**Version**: 0.0.1-SNAPSHOT  
**Author**: dongVu1105  
**Last Updated**: November 2025
