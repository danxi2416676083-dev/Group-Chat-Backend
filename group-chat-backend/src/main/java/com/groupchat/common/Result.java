package com.groupchat.common;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应结果封装类
 * 
 * 说明:
 * 1. 所有Controller接口都返回此类型的对象
 * 2. 包含状态码、消息、数据和时间戳
 * 3. 使用泛型支持不同类型的数据返回
 * 
 * @param <T> 响应数据的类型
 * @author GroupChat Team
 */
@Data
public class Result<T> {

    /**
     * 响应状态码
     * 200: 成功
     * 400: 请求参数错误
     * 401: 未授权
     * 403: 禁止访问
     * 404: 资源不存在
     * 500: 服务器内部错误
     */
    private Integer code;

    /**
     * 响应消息
     * 成功时为"success"或自定义成功消息
     * 失败时为错误描述
     */
    private String message;

    /**
     * 响应数据
     * 成功时包含返回的数据对象
     * 失败时为null或错误详情
     */
    private T data;

    /**
     * 响应时间戳
     * 格式: yyyy-MM-dd HH:mm:ss
     */
    private String timestamp;

    /**
     * 私有构造方法
     * 强制使用静态工厂方法创建实例
     */
    private Result() {
        // 生成当前时间戳
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 创建成功响应(无数据)
     * 
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 创建成功响应(带数据)
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 创建成功响应(带消息和数据)
     * 
     * @param message 成功消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 创建失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 创建失败响应(带数据)
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param data 错误详情数据
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 创建参数错误响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 参数错误响应对象
     */
    public static <T> Result<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 创建未授权响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 未授权响应对象
     */
    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 创建禁止访问响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 禁止访问响应对象
     */
    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 创建资源不存在响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 资源不存在响应对象
     */
    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 创建服务器错误响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 服务器错误响应对象
     */
    public static <T> Result<T> serverError(String message) {
        return error(500, message);
    }

    /**
     * 判断响应是否成功
     * 
     * @return true表示成功，false表示失败
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
