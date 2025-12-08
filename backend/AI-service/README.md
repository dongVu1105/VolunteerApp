# AI Service - Volunteer Hub

## 📋 Giới thiệu

AI Service là một microservice cung cấp khả năng trò chuyện thông minh (chatbot) sử dụng AI cho hệ thống Volunteer Hub. Service này tích hợp với Google Gemini AI và hỗ trợ lưu trữ lịch sử hội thoại để tạo trải nghiệm chat liên tục và thông minh.

## 🚀 Công nghệ sử dụng

### Core Technologies
- **Java 24** - Ngôn ngữ lập trình chính
- **Spring Boot 3.5.3** - Framework chính cho backend
- **Spring AI 1.0.0** - Framework tích hợp AI
- **Google Gemini 2.0 Flash** - Mô hình AI (qua OpenAI-compatible API)

### Database & Persistence
- **PostgreSQL** - Cơ sở dữ liệu chính
- **Spring Data JPA** - ORM framework
- **JDBC Chat Memory Repository** - Lưu trữ lịch sử chat

### Security
- **Spring Security** - Bảo mật ứng dụng
- **OAuth2 Resource Server** - Xác thực JWT
- **BCrypt** - Mã hóa mật khẩu

### Development Tools
- **Lombok** - Giảm boilerplate code
- **MapStruct** - Object mapping
- **Maven** - Build tool
- **Docker Compose** - Container orchestration

## ✨ Các chức năng chính

### 1. Chat với AI (Text Only)
- **Endpoint**: `POST /chatbot/chat/only-text`
- **Mô tả**: Trò chuyện với AI chỉ sử dụng văn bản
- **Tính năng**:
  - Hỗ trợ nhiều cuộc hội thoại độc lập (conversation ID)
  - Lưu trữ lịch sử tối đa 30 tin nhắn gần nhất
  - AI có ngữ cảnh từ các tin nhắn trước đó
  - Custom system prompt cho PetStory AI support

### 2. Chat với AI (Text + Media)
- **Endpoint**: `POST /chatbot/chat/with-media`
- **Mô tả**: Trò chuyện với AI kèm theo hình ảnh hoặc file media
- **Tính năng**:
  - Phân tích và trả lời dựa trên cả văn bản và hình ảnh
  - Hỗ trợ nhiều định dạng file media
  - Temperature điều chỉnh độ sáng tạo của AI (0.0 - chính xác nhất)

### 3. Quản lý lịch sử hội thoại
- Lưu trữ lịch sử chat trong PostgreSQL
- Tự động khởi tạo schema database
- Quản lý conversation theo ID
- Memory window giới hạn 30 tin nhắn để tối ưu hiệu năng

### 4. Bảo mật & Authentication
- Xác thực JWT cho tất cả requests
- Custom JWT decoder
- Role-based access control
- Secured endpoints với OAuth2

## 🌟 Điểm nổi bật

### 1. **Tích hợp Spring AI Framework**
- Sử dụng framework Spring AI mới nhất (1.0.0)
- Dễ dàng mở rộng và tích hợp với nhiều AI provider khác
- Code clean và dễ maintain

### 2. **Chat Memory Management**
- Lưu trữ lịch sử hội thoại trong database
- Message window với giới hạn 30 tin nhắn
- Context-aware conversations - AI nhớ ngữ cảnh cuộc trò chuyện
- Persistent memory across sessions

### 3. **Multimodal AI Support**
- Hỗ trợ cả văn bản và hình ảnh
- Tận dụng khả năng đa phương thức của Gemini 2.0 Flash
- Xử lý file upload an toàn với MultipartFile

### 4. **Security-First Design**
- JWT authentication cho mọi request
- Custom JWT decoder cho flexible validation
- OAuth2 resource server pattern
- Proper error handling với custom exception

### 5. **Scalable Architecture**
- Microservice architecture
- Stateless design (state lưu trong DB)
- Docker-ready với docker-compose
- Dễ dàng scale horizontal

### 6. **Developer-Friendly**
- Lombok giảm boilerplate code
- MapStruct cho object mapping tự động
- Clean architecture với separation of concerns
- Comprehensive error handling

### 7. **Modern Java 24**
- Sử dụng Java 24 với các tính năng mới nhất
- Pattern matching và record classes
- Virtual threads ready

### 8. **Production-Ready**
- Health checks và monitoring
- Database migration tự động
- Validation layers
- Proper logging và error messages

## 🛠️ Cấu hình

### Application Properties
```yaml
Server: Port 8086
Context Path: /chatbot
Database: PostgreSQL (localhost:5432)
AI Model: Gemini 2.0 Flash
Chat Memory: JDBC-based, auto-initialize schema
```

### Environment Variables
- `POSTGRES_USER`: postgres
- `POSTGRES_PASSWORD`: postgres
- `OPENAI_API_KEY`: API key cho Gemini AI

## 📦 Cài đặt và chạy

### Prerequisites
- Java 24
- Maven 3.x
- Docker & Docker Compose
- PostgreSQL (hoặc dùng Docker)

### Bước 1: Start Database
```bash
docker-compose up -d
```

### Bước 2: Build Project
```bash
./mvnw clean install
```

### Bước 3: Run Application
```bash
./mvnw spring-boot:run
```

Service sẽ chạy tại: `http://localhost:8086/chatbot`

## 📝 API Endpoints

### 1. Chat Only Text
```http
POST /chatbot/chat/only-text
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "conversationId": "user-123",
  "message": "Hello, how can you help me?"
}
```

### 2. Chat With Media
```http
POST /chatbot/chat/with-media
Content-Type: multipart/form-data
Authorization: Bearer <JWT_TOKEN>

file: <image_file>
message: "What do you see in this image?"
```

## 🔒 Security

Tất cả endpoints yêu cầu JWT token hợp lệ. Token phải được gửi trong Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📊 Database Schema

Service tự động tạo các bảng cần thiết để lưu trữ:
- Chat message history
- Conversation metadata
- User session information

## 🤖 AI Configuration

Service sử dụng Google Gemini 2.0 Flash model với:
- Temperature: 0.0 (cho media chat) - chính xác cao
- Default temperature cho text chat
- Max messages in memory: 30
- System prompt: Có thể tùy chỉnh theo nhu cầu

## 🎯 Use Cases

1. **Customer Support**: Trả lời tự động câu hỏi của tình nguyện viên
2. **Image Analysis**: Phân tích hình ảnh sự kiện, hoạt động
3. **Interactive Assistant**: Hỗ trợ người dùng trong quá trình đăng ký, tham gia sự kiện
4. **Content Moderation**: Kiểm tra nội dung bài đăng, bình luận

## 📄 License

[License information here]

## 👥 Contact

For more information, please contact the development team.

---

**Built with ❤️ by DongVu1105**
