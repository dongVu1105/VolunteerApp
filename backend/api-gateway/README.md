# API Gateway - Volunteer Hub

## 📋 Giới thiệu

API Gateway là cổng vào trung tâm cho toàn bộ hệ thống microservices của Volunteer Hub. Service này đóng vai trò quan trọng trong việc định tuyến requests, xác thực người dùng, và quản lý traffic giữa client và các backend services.

## 🚀 Công nghệ sử dụng

### Core Technologies
- **Java 24** - Ngôn ngữ lập trình chính
- **Spring Boot 3.2.5** - Framework chính
- **Spring Cloud Gateway** - Gateway framework reactive
- **Spring Cloud 2023.0.1** - Spring Cloud dependencies

### Reactive Stack
- **Project Reactor** - Reactive programming
- **WebFlux** - Reactive web framework
- **Netty** - Asynchronous event-driven network framework

### Communication
- **WebClient** - Reactive HTTP client
- **HTTP Service Proxy** - Declarative HTTP client

### Development Tools
- **Lombok** - Giảm boilerplate code
- **Maven** - Build tool
- **Jackson** - JSON processing

## ✨ Các chức năng chính

### 1. API Gateway & Routing
Định tuyến requests đến các microservices tương ứng:

- **Identity Service** (Port 8080): `/api/v1/identity/**`
  - Xác thực, phân quyền, quản lý người dùng
  
- **Profile Service** (Port 8081): `/api/v1/profile/user/**`
  - Quản lý thông tin profile người dùng
  
- **File Service** (Port 8082): `/api/v1/file/**`
  - Upload, download, quản lý files
  
- **Notification Service** (Port 8083): `/api/v1/notification/**`
  - Gửi và quản lý thông báo
  
- **Event Service** (Port 8084): `/api/v1/event/**`
  - Quản lý sự kiện tình nguyện
  
- **Post Service** (Port 8085): `/api/v1/post/**`
  - Quản lý bài đăng, tin tức
  
- **AI Service** (Port 8086): `/api/v1/chatbot/**`
  - Chatbot AI hỗ trợ người dùng

### 2. Authentication & Authorization
- **Global Authentication Filter**: Kiểm tra token cho mọi request
- **JWT Token Validation**: Xác thực token thông qua Identity Service
- **Public Endpoints**: Cho phép truy cập không cần token
  - `/api/v1/identity/auth/**` - Đăng nhập, refresh token
  - `/api/v1/identity/user/register` - Đăng ký tài khoản

### 3. Token Introspection
- Validate JWT token với Identity Service
- Real-time token verification
- Reactive, non-blocking validation

### 4. CORS Management
- Cấu hình CORS toàn cục
- Cho phép tất cả origins (có thể tùy chỉnh cho production)
- Hỗ trợ tất cả HTTP methods và headers

### 5. Request/Response Handling
- Automatic path stripping (StripPrefix filter)
- Standardized error responses
- JSON response formatting

### 6. Load Balancing Ready
- URI-based routing
- Có thể tích hợp service discovery (Eureka, Consul)
- Scalable architecture

## 🌟 Điểm nổi bật

### 1. **Reactive Architecture**
- Non-blocking I/O với Project Reactor
- Hiệu năng cao, xử lý concurrent requests tốt
- Memory footprint thấp hơn traditional blocking approach
- Scalability vượt trội

### 2. **Centralized Authentication**
- Single point of authentication
- Giảm code duplication ở các microservices
- Dễ dàng thay đổi authentication logic
- Consistent security policy

### 3. **Smart Routing**
- Path-based routing với predicates
- Automatic path transformation
- Flexible routing configuration
- Support cho dynamic routing

### 4. **Security-First Design**
- Global filter với highest priority (order = -1)
- Comprehensive token validation
- Public/Private endpoint separation
- Standardized error responses (401 Unauthorized)

### 5. **Microservices Gateway Pattern**
- Single entry point cho toàn bộ hệ thống
- Abstraction layer giữa client và services
- Simplified client integration
- Version management tập trung

### 6. **Declarative HTTP Client**
- HTTP Service Proxy với IdentityClient
- Type-safe API calls
- Clean, maintainable code
- Automatic serialization/deserialization

### 7. **Modern Spring Cloud Gateway**
- Spring Cloud Gateway (không phải Zuul)
- Built on WebFlux và Reactor
- Better performance và flexibility
- Active development và support

### 8. **Production-Ready Features**
- Comprehensive error handling
- Logging và monitoring support
- CORS configuration
- Easy to scale horizontally

### 9. **Clean Architecture**
- Clear separation of concerns
- Repository pattern cho external calls
- Service layer abstraction
- DTO pattern cho data transfer

### 10. **Developer-Friendly**
- Simple configuration với YAML
- Lombok giảm boilerplate
- Easy to add new routes
- Clear naming conventions

## 🛠️ Cấu hình

### Application Configuration
```yaml
Server Port: 8888
API Prefix: /api/v1
Routes: 7 microservices
Filter: StripPrefix (2 segments)
```

### Public Endpoints
```
- /api/v1/identity/auth/** (login, refresh, etc.)
- /api/v1/identity/user/register
```

### Service Endpoints
```
Identity:      http://localhost:8080
Profile:       http://localhost:8081
File:          http://localhost:8082
Notification:  http://localhost:8083
Event:         http://localhost:8084
Post:          http://localhost:8085
AI:            http://localhost:8086
```

## 📦 Cài đặt và chạy

### Prerequisites
- Java 24
- Maven 3.x
- Tất cả backend services đang chạy

### Bước 1: Build Project
```bash
./mvnw clean install
```

### Bước 2: Run Application
```bash
./mvnw spring-boot:run
```

Gateway sẽ chạy tại: `http://localhost:8888`

## 🔄 Request Flow

```
Client Request
    ↓
API Gateway (Port 8888)
    ↓
Authentication Filter
    ↓
Is Public Endpoint? → YES → Forward to Service
    ↓ NO
Extract JWT Token
    ↓
Introspect with Identity Service
    ↓
Valid Token? → YES → Forward to Service
    ↓ NO
Return 401 Unauthorized
```

## 📝 API Usage Examples

### 1. Public Endpoint (No Token)
```http
POST http://localhost:8888/api/v1/identity/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

### 2. Protected Endpoint (With Token)
```http
GET http://localhost:8888/api/v1/profile/user/me
Authorization: Bearer <jwt_token>
```

### 3. File Upload
```http
POST http://localhost:8888/api/v1/file/upload
Authorization: Bearer <jwt_token>
Content-Type: multipart/form-data

file: <file_data>
```

### 4. Chat with AI
```http
POST http://localhost:8888/api/v1/chatbot/chat/only-text
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "conversationId": "user-123",
  "message": "Hello!"
}
```

## 🔒 Security

### Authentication Flow
1. Client gửi request với JWT token trong Authorization header
2. Gateway extract token từ header
3. Gateway gọi Identity Service để validate token
4. Nếu valid: forward request đến service tương ứng
5. Nếu invalid: trả về 401 Unauthorized

### Error Response Format
```json
{
  "code": 1401,
  "message": "Unauthenticated"
}
```

## 🎯 Routing Configuration

### Path Transformation
```
Request:  /api/v1/identity/auth/login
Gateway:  Strip 2 prefixes (/api/v1)
Forward:  /identity/auth/login → http://localhost:8080
```

### Adding New Route
```yaml
- id: new-service
  uri: http://localhost:8087
  predicates:
    - Path=${app.api-prefix}/new-service/**
  filters:
    - StripPrefix=2
```

## 🚀 Performance Benefits

- **Reactive**: Non-blocking I/O, xử lý hàng ngàn requests đồng thời
- **Low Latency**: Netty-based, fast request handling
- **Resource Efficient**: Ít thread hơn traditional approach
- **Scalable**: Easy horizontal scaling

## 📊 Monitoring & Observability

Gateway hỗ trợ tích hợp với:
- Spring Boot Actuator
- Prometheus metrics
- Distributed tracing (Zipkin, Jaeger)
- Logging aggregation

## 🔧 Advanced Features (Có thể mở rộng)

- **Rate Limiting**: Giới hạn số requests per user/IP
- **Circuit Breaker**: Resilience4j integration
- **Service Discovery**: Eureka/Consul integration
- **Request/Response Modification**: Custom filters
- **Caching**: Redis cache integration
- **API Versioning**: Version-based routing

## 📚 Best Practices

1. **Always use HTTPS in production**
2. **Configure specific CORS origins** (không dùng `*`)
3. **Enable rate limiting** để tránh abuse
4. **Monitor gateway metrics** thường xuyên
5. **Implement circuit breaker** cho resilience
6. **Use service discovery** cho dynamic routing
7. **Log all authentication failures**

## 🎯 Use Cases

1. **Single Entry Point**: Client chỉ cần biết gateway URL
2. **Security Layer**: Centralized authentication/authorization
3. **Load Distribution**: Route traffic đến multiple instances
4. **API Versioning**: Manage multiple API versions
5. **Protocol Translation**: REST to gRPC, HTTP to WebSocket
6. **Request Aggregation**: Combine multiple backend calls

## 📄 Architecture Benefits

- **Decoupling**: Client không phụ thuộc vào internal service structure
- **Flexibility**: Dễ dàng thay đổi backend services
- **Security**: Single authentication point
- **Monitoring**: Centralized logging và metrics
- **Evolution**: API versioning và gradual migration

---

**Built with ❤️ by DongVu1105**
