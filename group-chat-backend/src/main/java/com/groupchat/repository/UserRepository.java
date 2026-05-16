package com.groupchat.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupchat.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问层
 * 
 * 说明:
 * 1. 继承MyBatis-Plus的BaseMapper，获得基础的CRUD方法
 * 2. 使用@Mapper注解标记为MyBatis映射器
 * 3. 可以在此添加自定义SQL方法
 * 
 * BaseMapper提供的方法:
 * - insert(T entity): 插入
 * - deleteById(Serializable id): 根据ID删除
 * - deleteByMap(Map<String, Object> columnMap): 根据条件删除
 * - delete(Wrapper<T> wrapper): 条件删除
 * - deleteBatchIds(Collection<? extends Serializable> idList): 批量删除
 * - updateById(T entity): 根据ID更新
 * - update(T entity, Wrapper<T> updateWrapper): 条件更新
 * - selectById(Serializable id): 根据ID查询
 * - selectBatchIds(Collection<? extends Serializable> idList): 批量查询
 * - selectByMap(Map<String, Object> columnMap): 条件查询
 * - selectOne(Wrapper<T> queryWrapper): 查询一条
 * - selectCount(Wrapper<T> queryWrapper): 查询总数
 * - selectList(Wrapper<T> queryWrapper): 条件查询列表
 * - selectMaps(Wrapper<T> queryWrapper): 查询Map列表
 * - selectPage(IPage<T> page, Wrapper<T> queryWrapper): 分页查询
 * 
 * @author GroupChat Team
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象，不存在返回null
     */
    @Select("SELECT * FROM gc_user WHERE username = #{username} AND deleted = 0")
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象，不存在返回null
     */
    @Select("SELECT * FROM gc_user WHERE email = #{email} AND deleted = 0")
    User findByEmail(String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户对象，不存在返回null
     */
    @Select("SELECT * FROM gc_user WHERE phone = #{phone} AND deleted = 0")
    User findByPhone(String phone);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 存在返回true，否则false
     */
    @Select("SELECT COUNT(*) > 0 FROM gc_user WHERE username = #{username} AND deleted = 0")
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 存在返回true，否则false
     */
    @Select("SELECT COUNT(*) > 0 FROM gc_user WHERE email = #{email} AND deleted = 0")
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 存在返回true，否则false
     */
    @Select("SELECT COUNT(*) > 0 FROM gc_user WHERE phone = #{phone} AND deleted = 0")
    boolean existsByPhone(String phone);
}
