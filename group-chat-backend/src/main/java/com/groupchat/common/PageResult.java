package com.groupchat.common;

import lombok.Data;
import java.util.List;

/**
 * 分页查询结果封装类
 * 
 * 说明:
 * 1. 用于封装分页查询的返回结果
 * 2. 包含当前页数据、分页信息(总页数、总条数等)
 * 3. 配合PageRequest使用
 * 
 * @param <T> 列表元素类型
 * @author GroupChat Team
 */
@Data
public class PageResult<T> {

    /**
     * 当前页数据列表
     */
    private List<T> list;

    /**
     * 当前页码(从1开始)
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 私有构造方法
     */
    private PageResult() {
    }

    /**
     * 创建分页结果
     * 
     * @param list 当前页数据
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param total 总记录数
     * @param <T> 数据类型
     * @return 分页结果对象
     */
    public static <T> PageResult<T> of(List<T> list, Integer pageNum, Integer pageSize, Long total) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        
        // 计算总页数
        result.setTotalPages((int) Math.ceil((double) total / pageSize));
        
        // 计算是否有下一页
        result.setHasNext(pageNum < result.getTotalPages());
        
        // 计算是否有上一页
        result.setHasPrevious(pageNum > 1);
        
        return result;
    }

    /**
     * 创建空分页结果
     * 
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param <T> 数据类型
     * @return 空分页结果对象
     */
    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return of(List.of(), pageNum, pageSize, 0L);
    }
}
