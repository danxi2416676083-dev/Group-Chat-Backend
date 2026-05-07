-- ============================================
-- 群聊系统数据库结构脚本
-- 数据库: group_chat
-- 字符集: utf8mb4 (支持emoji)
-- 说明: 包含用户、群组、消息等核心表结构
-- ============================================

-- 创建数据库(如果不存在)
CREATE DATABASE IF NOT EXISTS group_chat 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE group_chat;

-- ============================================
-- 1. 用户表 (gc_user)
-- 说明: 存储系统用户信息
-- ============================================
CREATE TABLE IF NOT EXISTS gc_user (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    -- 用户名, 唯一, 用于登录
    username VARCHAR(32) NOT NULL COMMENT '用户名',
    -- 密码, 加密存储
    password VARCHAR(128) NOT NULL COMMENT '加密密码',
    -- 昵称, 显示名称
    nickname VARCHAR(64) NOT NULL COMMENT '昵称',
    -- 头像URL
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    -- 邮箱
    email VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    -- 手机号
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    -- 性别: 0-未知, 1-男, 2-女
    gender TINYINT UNSIGNED DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    -- 个性签名
    signature VARCHAR(255) DEFAULT NULL COMMENT '个性签名',
    -- 状态: 0-禁用, 1-正常
    status TINYINT UNSIGNED DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    -- 最后登录时间
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    -- 最后登录IP
    last_login_ip VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 逻辑删除: 0-未删除, 1-已删除
    deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 唯一索引: 用户名
    UNIQUE KEY uk_username (username),
    -- 唯一索引: 邮箱
    UNIQUE KEY uk_email (email),
    -- 唯一索引: 手机号
    UNIQUE KEY uk_phone (phone),
    -- 普通索引: 状态(用于筛选)
    KEY idx_status (status),
    -- 普通索引: 创建时间(用于排序)
    KEY idx_created_at (created_at)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 群组表 (gc_group)
-- 说明: 存储群组信息
-- ============================================
CREATE TABLE IF NOT EXISTS gc_group (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '群组ID',
    -- 群组名称
    name VARCHAR(128) NOT NULL COMMENT '群组名称',
    -- 群组描述
    description VARCHAR(512) DEFAULT NULL COMMENT '群组描述',
    -- 群组头像
    avatar VARCHAR(255) DEFAULT NULL COMMENT '群组头像',
    -- 群主ID(外键关联gc_user)
    owner_id BIGINT UNSIGNED NOT NULL COMMENT '群主ID',
    -- 群组类型: 0-普通群, 1-企业群, 2-临时群
    type TINYINT UNSIGNED DEFAULT 0 COMMENT '群组类型: 0-普通群, 1-企业群, 2-临时群',
    -- 最大成员数
    max_members INT UNSIGNED DEFAULT 500 COMMENT '最大成员数',
    -- 当前成员数
    member_count INT UNSIGNED DEFAULT 1 COMMENT '当前成员数',
    -- 群组公告
    announcement TEXT DEFAULT NULL COMMENT '群组公告',
    -- 入群方式: 0-自由加入, 1-需要验证, 2-禁止加入, 3-邀请加入
    join_type TINYINT UNSIGNED DEFAULT 0 COMMENT '入群方式: 0-自由加入, 1-需要验证, 2-禁止加入, 3-邀请加入',
    -- 状态: 0-解散, 1-正常
    status TINYINT UNSIGNED DEFAULT 1 COMMENT '状态: 0-解散, 1-正常',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 逻辑删除
    deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 普通索引: 群主ID
    KEY idx_owner_id (owner_id),
    -- 普通索引: 群组类型
    KEY idx_type (type),
    -- 普通索引: 状态
    KEY idx_status (status),
    -- 普通索引: 创建时间
    KEY idx_created_at (created_at)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组表';

-- ============================================
-- 3. 群组成员表 (gc_group_member)
-- 说明: 存储群组成员关系
-- ============================================
CREATE TABLE IF NOT EXISTS gc_group_member (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '成员关系ID',
    -- 群组ID(外键关联gc_group)
    group_id BIGINT UNSIGNED NOT NULL COMMENT '群组ID',
    -- 用户ID(外键关联gc_user)
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    -- 成员角色: 0-成员, 1-管理员, 2-群主
    role TINYINT UNSIGNED DEFAULT 0 COMMENT '成员角色: 0-成员, 1-管理员, 2-群主',
    -- 群昵称(在群内的显示名称)
    group_nickname VARCHAR(64) DEFAULT NULL COMMENT '群昵称',
    -- 入群时间
    joined_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入群时间',
    -- 最后发言时间
    last_speak_time DATETIME DEFAULT NULL COMMENT '最后发言时间',
    -- 是否禁言: 0-否, 1-是
    muted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否禁言: 0-否, 1-是',
    -- 禁言截止时间
    mute_end_time DATETIME DEFAULT NULL COMMENT '禁言截止时间',
    -- 状态: 0-已退出, 1-正常
    status TINYINT UNSIGNED DEFAULT 1 COMMENT '状态: 0-已退出, 1-正常',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 唯一索引: 群组ID+用户ID(一个用户在一个群只能有一条记录)
    UNIQUE KEY uk_group_user (group_id, user_id),
    -- 普通索引: 用户ID(用于查询用户加入的所有群)
    KEY idx_user_id (user_id),
    -- 普通索引: 角色(用于筛选管理员)
    KEY idx_role (role),
    -- 普通索引: 状态
    KEY idx_status (status)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组成员表';

-- ============================================
-- 4. 消息表 (gc_message)
-- 说明: 存储聊天消息
-- ============================================
CREATE TABLE IF NOT EXISTS gc_message (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    -- 消息全局唯一标识(UUID)
    msg_id VARCHAR(64) NOT NULL COMMENT '消息UUID',
    -- 会话类型: 0-单聊, 1-群聊
    session_type TINYINT UNSIGNED NOT NULL COMMENT '会话类型: 0-单聊, 1-群聊',
    -- 群组ID(群聊时有效)
    group_id BIGINT UNSIGNED DEFAULT NULL COMMENT '群组ID',
    -- 发送者ID
    sender_id BIGINT UNSIGNED NOT NULL COMMENT '发送者ID',
    -- 接收者ID(单聊时有效)
    receiver_id BIGINT UNSIGNED DEFAULT NULL COMMENT '接收者ID',
    -- 消息类型: 0-文本, 1-图片, 2-语音, 3-视频, 4-文件, 5-系统消息
    msg_type TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '消息类型: 0-文本, 1-图片, 2-语音, 3-视频, 4-文件, 5-系统消息',
    -- 消息内容
    content TEXT NOT NULL COMMENT '消息内容',
    -- 额外数据(JSON格式,存储图片URL、文件信息等)
    extra_data JSON DEFAULT NULL COMMENT '额外数据(JSON)',
    -- 引用消息ID
    reply_to_msg_id VARCHAR(64) DEFAULT NULL COMMENT '引用消息ID',
    -- @的用户ID列表(JSON数组)
    at_user_ids JSON DEFAULT NULL COMMENT '@的用户ID列表',
    -- 是否已撤回: 0-否, 1-是
    recalled TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已撤回: 0-否, 1-是',
    -- 撤回时间
    recalled_at DATETIME DEFAULT NULL COMMENT '撤回时间',
    -- 发送时间
    send_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 唯一索引: 消息UUID
    UNIQUE KEY uk_msg_id (msg_id),
    -- 普通索引: 群组ID+发送时间(用于群消息查询)
    KEY idx_group_send_time (group_id, send_time),
    -- 普通索引: 发送者ID+发送时间(用于查询用户发送的消息)
    KEY idx_sender_send_time (sender_id, send_time),
    -- 普通索引: 接收者ID+发送时间(用于单聊消息查询)
    KEY idx_receiver_send_time (receiver_id, send_time),
    -- 普通索引: 消息类型
    KEY idx_msg_type (msg_type)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ============================================
-- 5. 好友关系表 (gc_friendship)
-- 说明: 存储用户好友关系
-- ============================================
CREATE TABLE IF NOT EXISTS gc_friendship (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '好友关系ID',
    -- 用户ID(发起者)
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    -- 好友ID
    friend_id BIGINT UNSIGNED NOT NULL COMMENT '好友ID',
    -- 好友备注名
    remark_name VARCHAR(64) DEFAULT NULL COMMENT '好友备注名',
    -- 好友分组
    group_name VARCHAR(64) DEFAULT '我的好友' COMMENT '好友分组',
    -- 申请状态: 0-待确认, 1-已通过, 2-已拒绝
    status TINYINT UNSIGNED DEFAULT 0 COMMENT '申请状态: 0-待确认, 1-已通过, 2-已拒绝',
    -- 申请消息
    apply_message VARCHAR(255) DEFAULT NULL COMMENT '申请消息',
    -- 成为好友时间
    became_friend_at DATETIME DEFAULT NULL COMMENT '成为好友时间',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 唯一索引: 用户ID+好友ID(避免重复添加)
    UNIQUE KEY uk_user_friend (user_id, friend_id),
    -- 普通索引: 好友ID
    KEY idx_friend_id (friend_id),
    -- 普通索引: 状态
    KEY idx_status (status)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友关系表';

-- ============================================
-- 6. 用户会话表 (gc_user_session)
-- 说明: 存储用户的会话列表(最近聊天)
-- ============================================
CREATE TABLE IF NOT EXISTS gc_user_session (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    -- 用户ID
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    -- 会话类型: 0-单聊, 1-群聊
    session_type TINYINT UNSIGNED NOT NULL COMMENT '会话类型: 0-单聊, 1-群聊',
    -- 目标ID(单聊为对方用户ID,群聊为群组ID)
    target_id BIGINT UNSIGNED NOT NULL COMMENT '目标ID',
    -- 置顶标识: 0-否, 1-是
    pinned TINYINT UNSIGNED DEFAULT 0 COMMENT '置顶: 0-否, 1-是',
    -- 免打扰: 0-否, 1-是
    do_not_disturb TINYINT UNSIGNED DEFAULT 0 COMMENT '免打扰: 0-否, 1-是',
    -- 最后一条消息ID
    last_msg_id VARCHAR(64) DEFAULT NULL COMMENT '最后消息ID',
    -- 最后消息内容预览
    last_msg_preview VARCHAR(255) DEFAULT NULL COMMENT '最后消息预览',
    -- 最后消息时间
    last_msg_time DATETIME DEFAULT NULL COMMENT '最后消息时间',
    -- 未读消息数
    unread_count INT UNSIGNED DEFAULT 0 COMMENT '未读消息数',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 唯一索引: 用户ID+会话类型+目标ID
    UNIQUE KEY uk_user_session (user_id, session_type, target_id),
    -- 普通索引: 最后消息时间(用于排序)
    KEY idx_last_msg_time (last_msg_time),
    -- 普通索引: 置顶+最后消息时间(用于置顶排序)
    KEY idx_pinned_time (pinned, last_msg_time)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- ============================================
-- 7. 系统通知表 (gc_notification)
-- 说明: 存储系统通知消息
-- ============================================
CREATE TABLE IF NOT EXISTS gc_notification (
    -- 主键ID, 自增
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    -- 接收者ID(0表示全局通知)
    receiver_id BIGINT UNSIGNED DEFAULT 0 COMMENT '接收者ID, 0为全局',
    -- 通知类型: 0-系统, 1-好友申请, 2-入群申请, 3-群邀请
    type TINYINT UNSIGNED NOT NULL COMMENT '通知类型',
    -- 通知标题
    title VARCHAR(128) NOT NULL COMMENT '通知标题',
    -- 通知内容
    content TEXT NOT NULL COMMENT '通知内容',
    -- 关联数据(JSON格式)
    extra_data JSON DEFAULT NULL COMMENT '关联数据',
    -- 是否已读: 0-否, 1-是
    is_read TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读: 0-否, 1-是',
    -- 创建时间
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 读取时间
    read_at DATETIME DEFAULT NULL COMMENT '读取时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    -- 普通索引: 接收者ID+是否已读(用于查询未读)
    KEY idx_receiver_read (receiver_id, is_read),
    -- 普通索引: 创建时间
    KEY idx_created_at (created_at)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统通知表';

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入测试用户(密码为123456的BCrypt加密结果)
INSERT INTO gc_user (username, password, nickname, avatar, email, phone, gender, signature, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 'admin@example.com', '13800138000', 1, '系统管理员', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user1', 'user1@example.com', '13800138001', 1, '大家好', 1),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user2', 'user2@example.com', '13800138002', 2, '很高兴认识大家', 1),
('user3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '王五', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user3', 'user3@example.com', '13800138003', 1, '请多关照', 1);

-- 插入测试群组
INSERT INTO gc_group (name, description, avatar, owner_id, type, max_members, member_count, join_type, status) VALUES
('技术交流群', 'Java技术交流，分享学习心得', 'https://api.dicebear.com/7.x/identicon/svg?seed=tech', 1, 0, 500, 3, 0, 1),
('生活闲聊群', '日常闲聊，放松心情', 'https://api.dicebear.com/7.x/identicon/svg?seed=life', 2, 0, 200, 2, 1, 1);

-- 插入群组成员
INSERT INTO gc_group_member (group_id, user_id, role, group_nickname, joined_at, status) VALUES
(1, 1, 2, '管理员', NOW(), 1),  -- 群主
(1, 2, 0, '张三', NOW(), 1),     -- 普通成员
(1, 3, 1, '李四(管理员)', NOW(), 1), -- 管理员
(2, 2, 2, '群主李四', NOW(), 1), -- 群主
(2, 3, 0, '王五', NOW(), 1);     -- 普通成员

-- 插入测试消息
INSERT INTO gc_message (msg_id, session_type, group_id, sender_id, msg_type, content, send_time) VALUES
('msg-001', 1, 1, 1, 0, '欢迎大家加入技术交流群！', NOW()),
('msg-002', 1, 1, 2, 0, '大家好，我是新来的张三', NOW()),
('msg-003', 1, 1, 3, 0, '欢迎张三！有什么Java问题可以问我', NOW());
