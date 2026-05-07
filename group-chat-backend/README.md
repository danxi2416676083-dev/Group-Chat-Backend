# 群聊系统开发文档

## 项目概述

本项目是一个完整的群聊系统后端，采用 Java + Spring Boot 3.x + MySQL + Redis + WebSocket 技术栈，支持类似 QQ/微信的群聊基本功能。

### 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 编程语言 | Java | JDK 21/24 |
| 框架 | Spring Boot | 3.2.0 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 6.0+ |
| ORM | MyBatis-Plus | 3.5.5 |
| 安全 | Spring Security + JWT | - |
| 实时通信 | WebSocket | - |
| 连接池 | Druid | 1.2.20 |

---

## 目录结构

```
group-chat-backend/
├── pom.xml                          # Maven构建配置
├── src/
│   └── main/
│       ├── java/com/groupchat/
│       │   ├── GroupChatApplication.java       # 应用入口
│       │   ├── config/                         # 配置类
│       │   │   ├── MyBatisPlusConfig.java     # MyBatis-Plus配置
│       │   │   └── RedisConfig.java           # Redis配置
│       │   ├── controller/                     # 控制器层
│       │   │   ├── AuthController.java        # 认证接口
│       │   │   ├── GroupController.java       # 群组接口
│       │   │   ├── MessageController.java     # 消息接口
│       │   │   └── UserController.java        # 用户接口
│       │   ├── service/                        # 服务层
│       │   │   ├── AuthService.java           # 认证服务
│       │   │   ├── GroupService.java          # 群组服务
│       │   │   └── MessageService.java        # 消息服务
│       │   ├── repository/                     # 数据访问层
│       │   │   ├── UserRepository.java        # 用户数据访问
│       │   │   ├── GroupRepository.java       # 群组数据访问
│       │   │   ├── GroupMemberRepository.java # 成员数据访问
│       │   │   └── MessageRepository.java     # 消息数据访问
│       │   ├── entity/                         # 实体类
│       │   │   ├── User.java                  # 用户实体
│       │   │   ├── Group.java                 # 群组实体
│       │   │   ├── GroupMember.java           # 成员实体
│       │   │   └── Message.java               # 消息实体
│       │   ├── dto/                            # 数据传输对象
│       │   │   ├── LoginRequest.java          # 登录请求
│       │   │   ├── LoginResponse.java         # 登录响应
│       │   │   ├── CreateGroupRequest.java    # 创建群组请求
│       │   │   ├── GroupResponse.java         # 群组响应
│       │   │   ├── SendMessageRequest.java    # 发送消息请求
│       │   │   └── MessageResponse.java       # 消息响应
│       │   ├── security/                       # 安全相关
│       │   │   ├── JwtTokenProvider.java      # JWT提供者
│       │   │   ├── JwtAuthenticationFilter.java # JWT过滤器
│       │   │   ├── CustomUserDetailsService.java # 用户详情服务
│       │   │   └── SecurityConfig.java        # 安全配置
│       │   ├── websocket/                      # WebSocket相关
│       │   │   ├── WebSocketServer.java       # WebSocket服务器
│       │   │   ├── WebSocketMessage.java      # WebSocket消息
│       │   │   └── WebSocketConfig.java       # WebSocket配置
│       │   ├── common/                         # 通用类
│       │   │   ├── Result.java                # 统一响应
│       │   │   └── PageResult.java            # 分页响应
│       │   ├── exception/                      # 异常处理
│       │   │   ├── BusinessException.java     # 业务异常
│       │   │   ├── ErrorCode.java             # 错误码
│       │   │   └── GlobalExceptionHandler.java # 全局异常处理
│       │   └── utils/                          # 工具类
│       └── resources/
│           ├── application.yml                # 应用配置
│           └── db/
│               └── schema.sql                 # 数据库脚本
```

---

## 快速开始

### 1. 环境准备

- **JDK**: OpenJDK 21 或 JDK 24
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Maven**: 3.8+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行数据库脚本
source d:/CodeBuddy/demo/group-chat-backend/src/main/resources/db/schema.sql
```

### 3. 配置修改

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/group_chat?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果没有密码留空
```

### 4. 编译运行

```bash
# 进入项目目录
cd d:/CodeBuddy/demo/group-chat-backend

# 编译
mvn clean package

# 运行
mvn spring-boot:run

# 或者运行jar
java -jar target/group-chat-backend-1.0.0.jar
```

### 5. 验证启动

- REST API: http://localhost:8080/api
- WebSocket: ws://localhost:8080/api/ws/chat
- Actuator: http://localhost:8080/api/actuator/health

---

## API接口文档

### 认证接口

#### 登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "123456"
}
```

响应：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "user": {
      "id": 1,
      "username": "user1",
      "nickname": "张三",
      "avatar": "https://..."
    }
  }
}
```

#### 注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "123456",
  "confirmPassword": "123456",
  "nickname": "新用户",
  "email": "newuser@example.com"
}
```

### 群组接口

#### 创建群组
```http
POST /api/groups
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "技术交流群",
  "description": "Java技术交流",
  "type": 0,
  "maxMembers": 500,
  "joinType": 0
}
```

#### 获取群组列表
```http
GET /api/groups
Authorization: Bearer {token}
```

#### 获取群组详情
```http
GET /api/groups/{groupId}
Authorization: Bearer {token}
```

#### 加入群组
```http
POST /api/groups/{groupId}/join
Authorization: Bearer {token}
```

#### 退出群组
```http
POST /api/groups/{groupId}/leave
Authorization: Bearer {token}
```

#### 获取群成员
```http
GET /api/groups/{groupId}/members
Authorization: Bearer {token}
```

#### 踢出成员
```http
POST /api/groups/{groupId}/members/{userId}/kick
Authorization: Bearer {token}
```

### 消息接口

#### 发送消息
```http
POST /api/messages
Authorization: Bearer {token}
Content-Type: application/json

{
  "msgId": "msg-uuid-123",
  "sessionType": 1,
  "groupId": 1,
  "msgType": 0,
  "content": "大家好！"
}
```

#### 获取群消息
```http
GET /api/messages/group/{groupId}?before=2024-01-01T00:00:00&limit=20
Authorization: Bearer {token}
```

#### 撤回消息
```http
POST /api/messages/{msgId}/recall
Authorization: Bearer {token}
```

---

## WebSocket协议

### 连接方式
```
ws://localhost:8080/api/ws/chat?token={jwt_token}
```

### 客户端发送消息

#### 心跳
```json
{
  "type": "ping"
}
```

#### 加入群组
```json
{
  "type": "join_group",
  "groupId": 1
}
```

#### 离开群组
```json
{
  "type": "leave_group",
  "groupId": 1
}
```

#### 正在输入
```json
{
  "type": "typing",
  "groupId": 1
}
```

### 服务端推送消息

#### 连接成功
```json
{
  "type": "connect_success",
  "senderId": 1,
  "senderName": "user1",
  "data": "WebSocket连接成功",
  "timestamp": "2024-01-01 12:00:00"
}
```

#### 新消息
```json
{
  "type": "chat_message",
  "senderId": 1,
  "groupId": 1,
  "data": {
    "msgId": "msg-uuid-123",
    "senderId": 1,
    "senderNickname": "张三",
    "msgType": 0,
    "content": "大家好！",
    "sendTime": "2024-01-01 12:00:00"
  },
  "timestamp": "2024-01-01 12:00:00"
}
```

#### 消息撤回
```json
{
  "type": "recall_message",
  "senderId": 1,
  "groupId": 1,
  "data": {
    "msgId": "msg-uuid-123",
    "operatorId": 1
  },
  "timestamp": "2024-01-01 12:00:00"
}
```

---

## 前端集成

### Pinia Store 使用

#### 1. 安装依赖
```bash
npm install pinia
```

#### 2. 创建 Store
```javascript
// stores/auth.js
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()

// 登录
await authStore.login({ username: 'xxx', password: 'xxx' })

// 获取用户信息
console.log(authStore.userInfo)
console.log(authStore.isLoggedIn)
```

#### 3. WebSocket 集成
```javascript
// stores/websocket.js
import { useWebSocketStore } from './stores/websocket'

const wsStore = useWebSocketStore()

// 连接
wsStore.connect()

// 发送消息
wsStore.send({ type: 'ping' })

// 断开
wsStore.disconnect()
```

### Vue 组件示例

```vue
<template>
  <div class="chat-container">
    <!-- 群组列表 -->
    <div class="group-list">
      <div
        v-for="group in chatStore.groups"
        :key="group.id"
        @click="enterGroup(group.id)"
      >
        {{ group.name }}
      </div>
    </div>
    
    <!-- 消息列表 -->
    <div class="message-list">
      <div
        v-for="msg in chatStore.currentMessages"
        :key="msg.msgId"
        :class="{ 'self': msg.isSelf }"
      >
        <img :src="msg.senderAvatar" />
        <span>{{ msg.senderNickname }}</span>
        <p>{{ msg.content }}</p>
      </div>
    </div>
    
    <!-- 输入框 -->
    <div class="input-area">
      <input v-model="messageText" @keyup.enter="sendMessage" />
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useChatStore } from './stores/chat'
import { useWebSocketStore } from './stores/websocket'

const chatStore = useChatStore()
const wsStore = useWebSocketStore()
const messageText = ref('')

onMounted(() => {
  // 加载群组列表
  chatStore.loadGroups()
  // 连接WebSocket
  wsStore.connect()
})

onUnmounted(() => {
  wsStore.disconnect()
})

const enterGroup = (groupId) => {
  chatStore.enterGroup(groupId)
}

const sendMessage = async () => {
  if (!messageText.value.trim()) return
  
  await chatStore.sendTextMessage(messageText.value)
  messageText.value = ''
}
</script>
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 100101 | 用户不存在 |
| 100102 | 用户已存在 |
| 100106 | 密码错误 |
| 200101 | 群组不存在 |
| 200104 | 群成员已满 |
| 200106 | 已是群成员 |
| 200109 | 没有群组操作权限 |
| 300101 | 消息不存在 |

---

## 部署说明

### 生产环境配置

1. **修改配置文件**
   - 使用环境变量或外部配置文件
   - 修改数据库连接为生产数据库
   - 配置Redis集群
   - 修改JWT密钥为复杂密钥

2. **构建部署包**
```bash
mvn clean package -DskipTests
```

3. **运行**
```bash
java -jar -Dspring.profiles.active=prod target/group-chat-backend-1.0.0.jar
```

### Docker部署

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/group-chat-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t group-chat-backend .
docker run -p 8080:8080 group-chat-backend
```

---

## 开发团队

- **项目**: 群聊系统后端
- **版本**: 1.0.0
- **技术栈**: Java + Spring Boot + MySQL + Redis + WebSocket
- **JDK版本**: 21/24

---

## 许可证

MIT License
