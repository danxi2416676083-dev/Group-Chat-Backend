package com.groupchat.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupchat.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 群组成员数据访问层
 * 
 * 说明:
 * 1. 继承MyBatis-Plus的BaseMapper，获得基础的CRUD方法
 * 2. 提供群组成员相关的自定义查询方法
 * 
 * @author GroupChat Team
 */
@Mapper
public interface GroupMemberRepository extends BaseMapper<GroupMember> {

    /**
     * 根据群组ID和用户ID查询成员关系
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 成员关系对象
     */
    @Select("SELECT * FROM gc_group_member WHERE group_id = #{groupId} AND user_id = #{userId} AND status = 1")
    GroupMember findByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

    /**
     * 查询群组的所有成员
     * 
     * @param groupId 群组ID
     * @return 成员列表
     */
    @Select("SELECT * FROM gc_group_member WHERE group_id = #{groupId} AND status = 1 ORDER BY role DESC, joined_at ASC")
    List<GroupMember> findByGroupId(Long groupId);

    /**
     * 查询用户加入的所有群组
     * 
     * @param userId 用户ID
     * @return 成员关系列表
     */
    @Select("SELECT * FROM gc_group_member WHERE user_id = #{userId} AND status = 1 ORDER BY joined_at DESC")
    List<GroupMember> findByUserId(Long userId);

    /**
     * 查询群组的成员数量
     * 
     * @param groupId 群组ID
     * @return 成员数量
     */
    @Select("SELECT COUNT(*) FROM gc_group_member WHERE group_id = #{groupId} AND status = 1")
    int countByGroupId(Long groupId);

    /**
     * 查询群组的管理员列表(包括群主)
     * 
     * @param groupId 群组ID
     * @return 管理员列表
     */
    @Select("SELECT * FROM gc_group_member WHERE group_id = #{groupId} AND role IN (1, 2) AND status = 1")
    List<GroupMember> findAdminsByGroupId(Long groupId);

    /**
     * 更新成员角色
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param role 新角色
     * @return 影响行数
     */
    @Update("UPDATE gc_group_member SET role = #{role} WHERE group_id = #{groupId} AND user_id = #{userId}")
    int updateRole(@Param("groupId") Long groupId, @Param("userId") Long userId, @Param("role") Integer role);

    /**
     * 更新群昵称
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param groupNickname 群昵称
     * @return 影响行数
     */
    @Update("UPDATE gc_group_member SET group_nickname = #{groupNickname} WHERE group_id = #{groupId} AND user_id = #{userId}")
    int updateGroupNickname(@Param("groupId") Long groupId, @Param("userId") Long userId, @Param("groupNickname") String groupNickname);

    /**
     * 设置禁言
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param muted 是否禁言
     * @param muteEndTime 禁言截止时间
     * @return 影响行数
     */
    @Update("UPDATE gc_group_member SET muted = #{muted}, mute_end_time = #{muteEndTime} WHERE group_id = #{groupId} AND user_id = #{userId}")
    int updateMuteStatus(@Param("groupId") Long groupId, @Param("userId") Long userId, 
                         @Param("muted") Integer muted, @Param("muteEndTime") java.time.LocalDateTime muteEndTime);

    /**
     * 更新最后发言时间
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param lastSpeakTime 最后发言时间
     * @return 影响行数
     */
    @Update("UPDATE gc_group_member SET last_speak_time = #{lastSpeakTime} WHERE group_id = #{groupId} AND user_id = #{userId}")
    int updateLastSpeakTime(@Param("groupId") Long groupId, @Param("userId") Long userId, 
                            @Param("lastSpeakTime") java.time.LocalDateTime lastSpeakTime);

    /**
     * 退出群组(软删除)
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE gc_group_member SET status = 0 WHERE group_id = #{groupId} AND user_id = #{userId}")
    int leaveGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);

    /**
     * 检查用户是否在群组中
     * 
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 存在返回true
     */
    @Select("SELECT COUNT(*) > 0 FROM gc_group_member WHERE group_id = #{groupId} AND user_id = #{userId} AND status = 1")
    boolean existsByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
}
