package com.groupchat.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupchat.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 群组数据访问层
 * 
 * 说明:
 * 1. 继承MyBatis-Plus的BaseMapper，获得基础的CRUD方法
 * 2. 提供群组相关的自定义查询方法
 * 
 * @author GroupChat Team
 */
@Mapper
public interface GroupRepository extends BaseMapper<Group> {

    /**
     * 根据群主ID查询群组列表
     * 
     * @param ownerId 群主ID
     * @return 群组列表
     */
    @Select("SELECT * FROM gc_group WHERE owner_id = #{ownerId} AND deleted = 0 AND status = 1 ORDER BY created_at DESC")
    List<Group> findByOwnerId(Long ownerId);

    /**
     * 根据群组名称模糊查询
     * 
     * @param keyword 关键词
     * @return 群组列表
     */
    @Select("SELECT * FROM gc_group WHERE name LIKE CONCAT('%', #{keyword}, '%') AND deleted = 0 AND status = 1")
    List<Group> searchByName(String keyword);

    /**
     * 增加群成员数
     * 
     * @param groupId 群组ID
     * @param count 增加数量
     * @return 影响行数
     */
    @Update("UPDATE gc_group SET member_count = member_count + #{count} WHERE id = #{groupId}")
    int incrementMemberCount(Long groupId, int count);

    /**
     * 减少群成员数
     * 
     * @param groupId 群组ID
     * @param count 减少数量
     * @return 影响行数
     */
    @Update("UPDATE gc_group SET member_count = member_count - #{count} WHERE id = #{groupId}")
    int decrementMemberCount(Long groupId, int count);

    /**
     * 更新群组公告
     * 
     * @param groupId 群组ID
     * @param announcement 公告内容
     * @return 影响行数
     */
    @Update("UPDATE gc_group SET announcement = #{announcement} WHERE id = #{groupId}")
    int updateAnnouncement(Long groupId, String announcement);

    /**
     * 解散群组(软删除)
     * 
     * @param groupId 群组ID
     * @return 影响行数
     */
    @Update("UPDATE gc_group SET status = 0 WHERE id = #{groupId}")
    int dissolveGroup(Long groupId);
}
