package com.groupchat.controller;

import com.groupchat.common.Result;
import com.groupchat.dto.*;
import com.groupchat.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群组控制器
 * 
 * 说明:
 * 1. 处理群组创建、查询、管理等操作
 * 2. 处理群成员管理(拉人、踢人、设置权限等)
 * 3. 所有接口都需要JWT认证
 * 
 * 接口列表:
 * - POST /api/groups: 创建群组
 * - GET /api/groups: 获取我的群组列表
 * - GET /api/groups/{id}: 获取群组详情
 * - POST /api/groups/{id}/join: 加入群组
 * - POST /api/groups/{id}/leave: 退出群组
 * - GET /api/groups/{id}/members: 获取群成员列表
 * - POST /api/groups/{id}/members/{userId}/kick: 踢出成员
 * - POST /api/groups/{id}/members/{userId}/role: 设置成员角色
 * - POST /api/groups/{id}/members/{userId}/mute: 禁言成员
 * - DELETE /api/groups/{id}: 解散群组
 * 
 * @author GroupChat Team
 */
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    /**
     * 群组服务
     */
    private final GroupService groupService;

    /**
     * 创建群组
     * 
     * 请求示例:
     * POST /api/groups
     * {
     *   "name": "技术交流群",
     *   "description": "Java技术交流",
     *   "type": 0,
     *   "maxMembers": 500,
     *   "joinType": 0
     * }
     * 
     * @param userDetails 当前登录用户
     * @param request 创建请求
     * @return 群组信息
     */
    @PostMapping
    public Result<GroupResponse> createGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateGroupRequest request) {
        Long userId = Long.valueOf(userDetails.getUsername());
        GroupResponse response = groupService.createGroup(userId, request);
        return Result.success("创建成功", response);
    }

    /**
     * 获取我的群组列表
     * 
     * @param userDetails 当前登录用户
     * @return 群组列表
     */
    @GetMapping
    public Result<List<GroupResponse>> getMyGroups(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());
        List<GroupResponse> groups = groupService.getUserGroups(userId);
        return Result.success(groups);
    }

    /**
     * 获取群组详情
     * 
     * @param userDetails 当前登录用户
     * @param groupId 群组ID
     * @return 群组详情
     */
    @GetMapping("/{groupId}")
    public Result<GroupResponse> getGroupDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        Long userId = Long.valueOf(userDetails.getUsername());
        GroupResponse response = groupService.getGroupDetail(groupId, userId);
        return Result.success(response);
    }

    /**
     * 搜索群组
     * 
     * @param userDetails 当前登录用户
     * @param keyword 关键词
     * @return 群组列表
     */
    @GetMapping("/search")
    public Result<List<GroupResponse>> searchGroups(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String keyword) {
        Long userId = Long.valueOf(userDetails.getUsername());
        List<GroupResponse> groups = groupService.searchGroups(keyword, userId);
        return Result.success(groups);
    }

    /**
     * 加入群组
     *
     * @param userDetails 当前登录用户
     * @param groupId     群组ID
     * @return 成功响应
     */
    @PostMapping("/{groupId}/join")
    public Result<String> joinGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        Long userId = Long.valueOf(userDetails.getUsername());
        groupService.joinGroup(groupId, userId);
        return Result.success("加入成功");
    }

    /**
     * 退出群组
     *
     * @param userDetails 当前登录用户
     * @param groupId     群组ID
     * @return 成功响应
     */
    @PostMapping("/{groupId}/leave")
    public Result<String> leaveGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        Long userId = Long.valueOf(userDetails.getUsername());
        groupService.leaveGroup(groupId, userId);
        return Result.success("退出成功");
    }

    /**
     * 获取群成员列表
     * 
     * @param userDetails 当前登录用户
     * @param groupId 群组ID
     * @return 成员列表
     */
    @GetMapping("/{groupId}/members")
    public Result<List<GroupMemberResponse>> getGroupMembers(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        List<GroupMemberResponse> members = groupService.getGroupMembers(groupId);
        return Result.success(members);
    }

    /**
     * 踢出群成员
     *
     * @param userDetails  当前登录用户(操作者)
     * @param groupId      群组ID
     * @param targetUserId 目标用户ID
     * @return 成功响应
     */
    @PostMapping("/{groupId}/members/{targetUserId}/kick")
    public Result<String> kickMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @PathVariable Long targetUserId) {
        Long operatorId = Long.valueOf(userDetails.getUsername());
        groupService.kickMember(groupId, operatorId, targetUserId);
        return Result.success("踢出成功");
    }

    /**
     * 设置成员角色
     *
     * @param userDetails  当前登录用户(操作者)
     * @param groupId      群组ID
     * @param targetUserId 目标用户ID
     * @param role         新角色(0-成员, 1-管理员)
     * @return 成功响应
     */
    @PostMapping("/{groupId}/members/{targetUserId}/role")
    public Result<String> setMemberRole(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @PathVariable Long targetUserId,
            @RequestParam Integer role) {
        Long operatorId = Long.valueOf(userDetails.getUsername());
        groupService.setMemberRole(groupId, operatorId, targetUserId, role);
        return Result.success("设置成功");
    }

    /**
     * 禁言成员
     *
     * @param userDetails  当前登录用户(操作者)
     * @param groupId      群组ID
     * @param targetUserId 目标用户ID
     * @param minutes      禁言时长(分钟)，0表示取消禁言
     * @return 成功响应
     */
    @PostMapping("/{groupId}/members/{targetUserId}/mute")
    public Result<String> muteMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @PathVariable Long targetUserId,
            @RequestParam(defaultValue = "60") Integer minutes) {
        Long operatorId = Long.valueOf(userDetails.getUsername());
        groupService.muteMember(groupId, operatorId, targetUserId, minutes);
        return Result.success(minutes > 0 ? "禁言成功" : "取消禁言成功");
    }

    /**
     * 解散群组
     *
     * @param userDetails 当前登录用户(群主)
     * @param groupId     群组ID
     * @return 成功响应
     */
    @DeleteMapping("/{groupId}")
    public Result<String> dissolveGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId) {
        Long operatorId = Long.valueOf(userDetails.getUsername());
        groupService.dissolveGroup(groupId, operatorId);
        return Result.success("解散成功");
    }
}
