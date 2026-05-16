package com.groupchat.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupchat.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息数据访问层
 * 
 * 说明:
 * 1. 继承MyBatis-Plus的BaseMapper，获得基础的CRUD方法
 * 2. 提供消息相关的自定义查询方法
 * 
 * @author GroupChat Team
 */
@Mapper
public interface MessageRepository extends BaseMapper<Message> {

    /**
     * 根据消息UUID查询消息
     * 
     * @param msgId 消息UUID
     * @return 消息对象
     */
    @Select("SELECT * FROM gc_message WHERE msg_id = #{msgId}")
    Message findByMsgId(String msgId);

    /**
     * 查询群组的历史消息
     * 
     * @param groupId 群组ID
     * @param limit 查询数量
     * @return 消息列表
     */
    @Select("SELECT * FROM gc_message WHERE group_id = #{groupId} AND session_type = 1 ORDER BY send_time DESC LIMIT #{limit}")
    List<Message> findByGroupId(@Param("groupId") Long groupId, @Param("limit") Integer limit);

    /**
     * 查询群组的历史消息(分页)
     * 
     * @param groupId 群组ID
     * @param beforeTime 在此时间之前
     * @param limit 查询数量
     * @return 消息列表
     */
    @Select("SELECT * FROM gc_message WHERE group_id = #{groupId} AND session_type = 1 AND send_time < #{beforeTime} ORDER BY send_time DESC LIMIT #{limit}")
    List<Message> findByGroupIdBeforeTime(@Param("groupId") Long groupId, @Param("beforeTime") LocalDateTime beforeTime, @Param("limit") Integer limit);

    /**
     * 查询单聊消息
     * 
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @param limit 查询数量
     * @return 消息列表
     */
    @Select("SELECT * FROM gc_message WHERE session_type = 0 AND ((sender_id = #{userId} AND receiver_id = #{friendId}) OR (sender_id = #{friendId} AND receiver_id = #{userId})) ORDER BY send_time DESC LIMIT #{limit}")
    List<Message> findPrivateMessages(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("limit") Integer limit);

    /**
     * 查询用户发送的消息
     * 
     * @param senderId 发送者ID
     * @param limit 查询数量
     * @return 消息列表
     */
    @Select("SELECT * FROM gc_message WHERE sender_id = #{senderId} ORDER BY send_time DESC LIMIT #{limit}")
    List<Message> findBySenderId(@Param("senderId") Long senderId, @Param("limit") Integer limit);

    /**
     * 撤回消息
     * 
     * @param msgId 消息UUID
     * @param recalledAt 撤回时间
     * @return 影响行数
     */
    @Update("UPDATE gc_message SET recalled = 1, recalled_at = #{recalledAt} WHERE msg_id = #{msgId}")
    int recallMessage(@Param("msgId") String msgId, @Param("recalledAt") LocalDateTime recalledAt);

    /**
     * 查询群组消息数量
     * 
     * @param groupId 群组ID
     * @return 消息数量
     */
    @Select("SELECT COUNT(*) FROM gc_message WHERE group_id = #{groupId} AND session_type = 1")
    long countByGroupId(Long groupId);

    /**
     * 查询用户相关的消息数量
     * 
     * @param userId 用户ID
     * @return 消息数量
     */
    @Select("SELECT COUNT(*) FROM gc_message WHERE sender_id = #{userId} OR receiver_id = #{userId}")
    long countByUserId(Long userId);

    /**
     * 查询某时间之后的消息
     * 
     * @param groupId 群组ID
     * @param afterTime 在此时间之后
     * @return 消息列表
     */
    @Select("SELECT * FROM gc_message WHERE group_id = #{groupId} AND session_type = 1 AND send_time > #{afterTime} ORDER BY send_time ASC")
    List<Message> findByGroupIdAfterTime(@Param("groupId") Long groupId, @Param("afterTime") LocalDateTime afterTime);
}
