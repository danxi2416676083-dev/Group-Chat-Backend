package com.groupchat.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * 
 * 说明:
 * 1. 统一定义系统中所有错误码
 * 2. 错误码格式: 模块(2位) + 类型(2位) + 序号(2位)
 * 3. 例如: 100101 = 用户模块(10) + 参数错误(01) + 第1个错误(01)
 * 
 * @author GroupChat Team
 */
@Getter
public enum ErrorCode {

    // ==================== 通用错误 (00) ====================
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误"),
    
    // ==================== 用户模块 (10) ====================
    USER_NOT_FOUND(100101, "用户不存在"),
    USER_ALREADY_EXISTS(100102, "用户已存在"),
    USERNAME_EXISTS(100103, "用户名已存在"),
    EMAIL_EXISTS(100104, "邮箱已存在"),
    PHONE_EXISTS(100105, "手机号已存在"),
    PASSWORD_ERROR(100106, "密码错误"),
    ACCOUNT_DISABLED(100107, "账号已被禁用"),
    TOKEN_EXPIRED(100108, "Token已过期"),
    TOKEN_INVALID(100109, "Token无效"),
    
    // ==================== 群组模块 (20) ====================
    GROUP_NOT_FOUND(200101, "群组不存在"),
    GROUP_ALREADY_EXISTS(200102, "群组已存在"),
    GROUP_NAME_EXISTS(200103, "群组名称已存在"),
    GROUP_FULL(200104, "群成员已满"),
    GROUP_JOIN_TYPE_DENIED(200105, "该群不允许加入"),
    GROUP_ALREADY_MEMBER(200106, "已是群成员"),
    GROUP_NOT_MEMBER(200107, "不是群成员"),
    GROUP_OWNER_CANNOT_LEAVE(200108, "群主不能退出群聊"),
    GROUP_PERMISSION_DENIED(200109, "没有群组操作权限"),
    GROUP_MEMBER_MUTED(200110, "您已被禁言"),
    
    // ==================== 消息模块 (30) ====================
    MESSAGE_NOT_FOUND(300101, "消息不存在"),
    MESSAGE_SEND_FAILED(300102, "消息发送失败"),
    MESSAGE_RECALL_TIMEOUT(300103, "消息超过撤回时间"),
    MESSAGE_RECALL_DENIED(300104, "无权撤回此消息"),
    
    // ==================== 好友模块 (40) ====================
    FRIEND_NOT_FOUND(400101, "好友不存在"),
    FRIEND_ALREADY_EXISTS(400102, "已是好友"),
    FRIEND_APPLY_PENDING(400103, "好友申请已发送，等待对方确认"),
    FRIEND_APPLY_NOT_FOUND(400104, "好友申请不存在"),
    
    // ==================== 文件模块 (50) ====================
    FILE_UPLOAD_FAILED(500101, "文件上传失败"),
    FILE_TOO_LARGE(500102, "文件大小超过限制"),
    FILE_TYPE_NOT_ALLOWED(500103, "不支持的文件类型"),
    FILE_NOT_FOUND(500104, "文件不存在");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 构造方法
     * 
     * @param code 错误码
     * @param message 错误消息
     */
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
