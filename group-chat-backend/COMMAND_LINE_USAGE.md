# 群聊系统后端命令行使用说明

本文档整理本项目在命令行中的常用命令格式、启动流程、接口调用方式和使用注意事项。

> 项目目录：`d:/CodeBuddy/group/group-chat-backend`  
> 服务地址：`http://localhost:8080/api`  
> WebSocket 地址：`ws://localhost:8080/api/ws/chat?token=<ACCESS_TOKEN>`

---

## 1. 命令行约定

### 1.1 占位符说明

| 占位符 | 含义 |
|---|---|
| `<ACCESS_TOKEN>` | 登录或注册成功后返回的 `data.accessToken` |
| `<REFRESH_TOKEN>` | 登录或注册成功后返回的 `data.refreshToken` |
| `<GROUP_ID>` | 群组 ID，例如 `1` |
| `<USER_ID>` | 用户 ID，例如 `2` |
| `<MSG_ID>` | 消息 ID，例如 `msg-001` 或客户端生成的 UUID |
| `<KEYWORD>` | 搜索关键词，例如 `技术` |


后续需要认证的接口，都必须携带：

```http
Authorization: Bearer <ACCESS_TOKEN>
```

### 1.2 Windows 命令行换行说明

Windows `cmd` 中多行命令使用 `^` 续行：

```cmd
curl -X GET "http://localhost:8080/api/" ^
  -H "Accept: application/json"
```

PowerShell 中建议使用 `curl.exe`，避免 `curl` 被识别为 PowerShell 别名：

```powershell
curl.exe -X GET "http://localhost:8080/api/"
```

---

## 2. 环境检查命令

### 2.1 检查 Java

```cmd
java -version
```

要求：`JDK 21` 或 `JDK 24`。

### 2.2 检查 Maven

```cmd
mvn -version
```

要求：`Maven 3.8+`。

如果提示 `mvn` 无法识别，说明本机未安装 Maven 或未配置环境变量。

### 2.3 检查 MySQL

```cmd
mysql --version
```

要求：`MySQL 8.0+`。

### 2.4 检查 Redis

无密码 Redis：

```cmd
redis-cli ping
```

有密码 Redis：

```cmd
redis-cli -a <REDIS_PASSWORD> ping
```

返回 `PONG` 表示 Redis 可用。

---

## 3. 数据库初始化命令

### 3.1 登录 MySQL 后执行脚本

```cmd
mysql -u root -p
```

进入 MySQL 后执行：

```sql
source d:/CodeBuddy/group/group-chat-backend/src/main/resources/db/schema.sql
```

### 3.2 直接在命令行执行脚本

```cmd
mysql -u root -p < d:/CodeBuddy/group/group-chat-backend/src/main/resources/db/schema.sql
```

脚本会自动创建并使用数据库：

```sql
CREATE DATABASE IF NOT EXISTS group_chat;
USE group_chat;
```

会创建的主要表：

| 表名 | 说明 |
|---|---|
| `gc_user` | 用户表 |
| `gc_group` | 群组表 |
| `gc_group_member` | 群成员表 |
| `gc_message` | 消息表 |
| `gc_friendship` | 好友关系表 |
| `gc_user_session` | 用户会话表 |
| `gc_notification` | 系统通知表 |

脚本内置测试用户：`admin`、`user1`、`user2`、`user3`。

---

## 4. 项目构建与启动命令

### 4.1 进入项目目录

```cmd
cd /d d:\CodeBuddy\group\group-chat-backend
```

或：

```powershell
cd d:/CodeBuddy/group/group-chat-backend
```

### 4.2 编译项目

```cmd
mvn clean compile
```

### 4.3 运行测试

```cmd
mvn test
```

### 4.4 打包项目

执行测试并打包：

```cmd
mvn clean package
```

跳过测试打包：

```cmd
mvn clean package -DskipTests
```

### 4.5 开发方式启动

```cmd
mvn spring-boot:run
```

### 4.6 JAR 方式启动

```cmd
java -jar target/group-chat-backend-1.0.0.jar
```

指定生产环境配置启动：

```cmd
java -jar -Dspring.profiles.active=prod target/group-chat-backend-1.0.0.jar
```

### 4.7 启动成功标志

控制台出现类似内容表示后端启动成功：

```text
群聊系统后端服务启动成功!
访问地址: http://localhost:8080/api
WebSocket: ws://localhost:8080/api/ws/chat
```

---

## 5. 基础验证命令

### 5.1 API 根路径

```cmd
curl -X GET "http://localhost:8080/api/"
```

用途：检查后端服务是否可访问。

### 5.2 健康检查

```cmd
curl -X GET "http://localhost:8080/api/actuator/health"
```

用途：查看应用、数据库、Redis 等健康状态。

---

## 6. 登录、注册与 Token 命令

### 6.1 用户登录

```cmd
curl -X POST "http://localhost:8080/api/auth/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"user1\",\"password\":\"123456\"}"
```

返回中重点关注：

```json
{
  "data" : {
    "accessToken" : "...",
    "refreshToken" : "...",
    "tokenType" : "Bearer",
    "expiresIn" : 7200
  }
}
```

后续接口使用：

```http
Authorization: Bearer <ACCESS_TOKEN>
```

### 6.2 用户注册

```cmd
curl -X POST "http://localhost:8080/api/auth/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"123456\",\"confirmPassword\":\"123456\",\"nickname\":\"测试用户\",\"email\":\"test@example.com\",\"phone\":\"13800138888\"}"
```

字段说明：

| 字段 | 必填 | 说明 |
|---|---|---|
| `username` | 是 | 4-20 位字母、数字、下划线 |
| `password` | 是 | 6-20 位 |
| `confirmPassword` | 是 | 必须与 `password` 一致 |
| `nickname` | 是 | 2-20 位 |
| `email` | 否 | 邮箱格式 |
| `phone` | 否 | 中国大陆手机号格式 |

注册成功后会自动登录，并返回 `accessToken` 和 `refreshToken`。

### 6.3 刷新 Token

```cmd
curl -X POST "http://localhost:8080/api/auth/refresh" ^
  -H "Authorization: Bearer <REFRESH_TOKEN>"
```

用途：当 `accessToken` 过期时，使用 `refreshToken` 获取新的令牌。

---

## 7. 用户接口命令

### 7.1 获取当前登录用户信息

```cmd
curl -X GET "http://localhost:8080/api/users/me" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

用途：验证 Token 是否有效，并查看当前用户资料。

---

## 8. 群组接口命令

### 8.1 创建群组

```cmd
curl -X POST "http://localhost:8080/api/groups" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>" ^
  -d "{\"name\":\"Java技术交流群\",\"description\":\"Java技术交流\",\"avatar\":\"\",\"type\":0,\"maxMembers\":500,\"joinType\":0,\"initialMembers\":[2,3]}"
```

字段说明：

| 字段 | 必填 | 说明 |
|---|---|---|
| `name` | 是 | 群组名称，1-50 位 |
| `description` | 否 | 群组描述，最多 200 位 |
| `avatar` | 否 | 群头像 URL |
| `type` | 否 | `0` 普通群，`1` 企业群，`2` 临时群，默认 `0` |
| `maxMembers` | 否 | 最大成员数，默认 `500` |
| `joinType` | 否 | `0` 自由加入，`1` 需要验证，`2` 禁止加入，`3` 邀请加入，默认 `0` |
| `initialMembers` | 否 | 初始成员 ID 数组 |

### 8.2 获取我的群组列表

```cmd
curl -X GET "http://localhost:8080/api/groups" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.3 获取群组详情

```cmd
curl -X GET "http://localhost:8080/api/groups/<GROUP_ID>" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

示例：

```cmd
curl -X GET "http://localhost:8080/api/groups/1" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.4 搜索群组

```cmd
curl -X GET "http://localhost:8080/api/groups/search?keyword=<KEYWORD>" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

示例：

```cmd
curl -X GET "http://localhost:8080/api/groups/search?keyword=技术" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.5 加入群组

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/join" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.6 退出群组

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/leave" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.7 获取群成员列表

```cmd
curl -X GET "http://localhost:8080/api/groups/<GROUP_ID>/members" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.8 踢出群成员

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/members/<USER_ID>/kick" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

说明：需要操作者具备群主管理权限。

### 8.9 设置成员角色

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/members/<USER_ID>/role?role=1" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

角色说明：

| 值 | 含义 |
|---|---|
| `0` | 普通成员 |
| `1` | 管理员 |

### 8.10 禁言或取消禁言成员

禁言 60 分钟：

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/members/<USER_ID>/mute?minutes=60" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

取消禁言：

```cmd
curl -X POST "http://localhost:8080/api/groups/<GROUP_ID>/members/<USER_ID>/mute?minutes=0" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 8.11 解散群组

```cmd
curl -X DELETE "http://localhost:8080/api/groups/<GROUP_ID>" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

说明：通常只有群主可执行。

---

## 9. 消息接口命令

### 9.1 发送群聊文本消息

```cmd
curl -X POST "http://localhost:8080/api/messages" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>" ^
  -d "{\"msgId\":\"msg-cmd-001\",\"sessionType\":1,\"groupId\":1,\"msgType\":0,\"content\":\"大家好，这是一条命令行发送的群聊消息\"}"
```

### 9.2 发送单聊文本消息

```cmd
curl -X POST "http://localhost:8080/api/messages" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>" ^
  -d "{\"msgId\":\"msg-cmd-private-001\",\"sessionType\":0,\"receiverId\":2,\"msgType\":0,\"content\":\"你好，这是一条命令行发送的单聊消息\"}"
```

发送消息字段说明：

| 字段 | 必填 | 说明 |
|---|---|---|
| `msgId` | 是 | 客户端生成的唯一消息 ID，用于去重 |
| `sessionType` | 是 | `0` 单聊，`1` 群聊 |
| `groupId` | 群聊必填 | 群组 ID |
| `receiverId` | 单聊必填 | 接收者用户 ID |
| `msgType` | 是 | `0` 文本，`1` 图片，`2` 语音，`3` 视频，`4` 文件，`5` 系统消息 |
| `content` | 是 | 消息内容或资源描述 |
| `extraData` | 否 | 额外 JSON 字符串，例如图片 URL、文件信息等 |
| `replyToMsgId` | 否 | 回复某条消息时填写被回复消息 ID |
| `atUserIds` | 否 | @ 的用户 ID 数组 |

### 9.3 发送带 @ 的群聊消息

```cmd
curl -X POST "http://localhost:8080/api/messages" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>" ^
  -d "{\"msgId\":\"msg-cmd-at-001\",\"sessionType\":1,\"groupId\":1,\"msgType\":0,\"content\":\"@2 请看这条消息\",\"atUserIds\":[2]}"
```

### 9.4 获取群组历史消息

默认获取 20 条：

```cmd
curl -X GET "http://localhost:8080/api/messages/group/<GROUP_ID>" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

指定数量：

```cmd
curl -X GET "http://localhost:8080/api/messages/group/<GROUP_ID>?limit=50" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

按时间向前分页：

```cmd
curl -X GET "http://localhost:8080/api/messages/group/<GROUP_ID>?beforeTime=2026-04-30%2010:00:00&limit=20" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

说明：`beforeTime` 格式为 `yyyy-MM-dd HH:mm:ss`，URL 中空格需要写成 `%20`。

### 9.5 获取单聊消息

```cmd
curl -X GET "http://localhost:8080/api/messages/private/<USER_ID>?limit=20" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 9.6 撤回消息

```cmd
curl -X POST "http://localhost:8080/api/messages/<MSG_ID>/recall" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 9.7 获取消息详情

```cmd
curl -X GET "http://localhost:8080/api/messages/<MSG_ID>" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 10. WebSocket 命令行测试

WebSocket 连接虽然路径公开，但连接时必须在 URL 参数中携带有效 `accessToken`。

### 10.1 使用 `wscat` 测试

安装：

```cmd
npm install -g wscat
```

连接：

```cmd
wscat -c "ws://localhost:8080/api/ws/chat?token=<ACCESS_TOKEN>"
```

也可以不全局安装，直接使用：

```cmd
npx wscat -c "ws://localhost:8080/api/ws/chat?token=<ACCESS_TOKEN>"
```

### 10.2 WebSocket 心跳

连接成功后输入：

```json
{"type":"ping"}
```

服务端应返回：

```json
{
  "type" : "pong",
  "data" : "pong"
}
```

### 10.3 加入群组实时频道

```json
{"type":"join_group","groupId":1}
```

### 10.4 离开群组实时频道

```json
{"type":"leave_group","groupId":1}
```

### 10.5 正在输入通知

群聊正在输入：

```json
{"type":"typing","groupId":1}
```

单聊正在输入：

```json
{"type":"typing","receiverId":2}
```

---

## 11. Docker 命令

README 中给出了 Dockerfile 示例。如果已在项目根目录创建 `Dockerfile`，可使用以下命令。

### 11.1 构建镜像

```cmd
docker build -t group-chat-backend .
```

### 11.2 运行容器

```cmd
docker run -p 8080:8080 group-chat-backend
```

说明：当前项目目录中未发现实际 `Dockerfile` 文件，如需使用 Docker，请先按 README 中的示例创建。

---

## 12. 常见问题与排查命令

### 12.1 接口返回 403 Forbidden

常见原因：

1. 没有携带 `Authorization` 请求头。
2. 请求头中仍然写着 `YOUR_ACCESS_TOKEN`，没有替换成真实 Token。
3. Token 已过期，需要调用 `/api/auth/refresh` 刷新。
4. 当前用户没有执行该操作的权限，例如非群主执行解散群组。

检查当前用户是否登录：

```cmd
curl -X GET "http://localhost:8080/api/users/me" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 12.2 接口返回 400 参数错误

常见原因：JSON 字段缺失、字段名写错、格式不符合校验规则。

建议检查：

- `Content-Type` 是否为 `application/json`
- JSON 中双引号是否转义正确
- 必填字段是否填写完整

### 12.3 数据库连接失败

检查 MySQL 是否运行：

```cmd
mysql -u root -p
```

检查数据库是否存在：

```sql
SHOW DATABASES;
USE group_chat;
SHOW TABLES;
```

### 12.4 Redis 连接失败

```cmd
redis-cli ping
```

或：

```cmd
redis-cli -a <REDIS_PASSWORD> ping
```

### 12.5 端口被占用

Windows 查看 8080 端口：

```cmd
netstat -ano | findstr :8080
```

结束指定进程：

```cmd
taskkill /PID <PID> /F
```

---

## 13. 推荐的完整命令行测试流程

### 第一步：启动后端

```cmd
cd /d d:\CodeBuddy\group\group-chat-backend
mvn spring-boot:run
```

### 第二步：检查服务

```cmd
curl -X GET "http://localhost:8080/api/"
```

### 第三步：登录获取 Token

```cmd
curl -X POST "http://localhost:8080/api/auth/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"user1\",\"password\":\"123456\"}"
```

复制响应中的 `data.accessToken`。

### 第四步：查询当前用户

```cmd
curl -X GET "http://localhost:8080/api/users/me" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 第五步：查询群组

```cmd
curl -X GET "http://localhost:8080/api/groups" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 第六步：发送群消息

```cmd
curl -X POST "http://localhost:8080/api/messages" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>" ^
  -d "{\"msgId\":\"msg-cmd-test-001\",\"sessionType\":1,\"groupId\":1,\"msgType\":0,\"content\":\"命令行测试消息\"}"
```

### 第七步：查询群消息

```cmd
curl -X GET "http://localhost:8080/api/messages/group/1?limit=20" ^
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```
