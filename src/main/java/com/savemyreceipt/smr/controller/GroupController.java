package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.DTO.group.request.GroupRequestDto;
import com.savemyreceipt.smr.DTO.group.response.GroupListResponseDto;
import com.savemyreceipt.smr.DTO.group.response.GroupResponseDto;
import com.savemyreceipt.smr.DTO.member.response.MemberListResponseDto;
import com.savemyreceipt.smr.DTO.receipt.response.ReceiptListResponseDto;
import com.savemyreceipt.smr.enums.Role;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Group", description = "그룹 관리 API")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 조회", description = "회원이 속한 그룹을 조회합니다.")
    @GetMapping
    public ApiResponseDto<GroupListResponseDto> getGroups(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_SUCCESS, groupService.getGroups(user.getUsername(), page));
    }

    @Operation(summary = "그룹 검색", description = "그룹 가입을 위해서 그룹을 검색합니다.")
    @GetMapping("/search")
    public ApiResponseDto<GroupListResponseDto> searchGroup(@AuthenticationPrincipal User user,
        @RequestParam String keyword, @RequestParam(defaultValue = "0") int page) {
        return ApiResponseDto.success(SuccessStatus.SEARCH_GROUP_SUCCESS, groupService.searchGroup(keyword, page));
    }

    @Operation(summary = "그룹 생성", description = "그룹을 생성합니다. 생성한 사람이 회계가 됩니다.")
    @PostMapping
    public ApiResponseDto<?> createGroup(@AuthenticationPrincipal User user,
        @RequestBody GroupRequestDto groupRequestDto) {
        groupService.createGroup(user.getUsername(), groupRequestDto);
        return ApiResponseDto.success(SuccessStatus.CREATE_GROUP_SUCCESS);
    }

    @Operation(summary = "그룹 멤버 조회", description = "그룹의 멤버를 조회합니다.")
    @GetMapping("/{groupId}/members")
    public ApiResponseDto<MemberListResponseDto> getGroupMembers(@PathVariable Long groupId, @RequestParam(defaultValue = "0") int page) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_MEMBER_SUCCESS, groupService.getGroupMembers(groupId, page));
    }

    @Operation(summary = "그룹 가입", description = "그룹에 가입합니다.")
    @PostMapping("/{groupId}/members")
    public ApiResponseDto<?> joinGroup(@Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long groupId, @RequestParam Role role) {
        groupService.joinGroup(user.getUsername(), groupId, role);
        return ApiResponseDto.success(SuccessStatus.JOIN_GROUP_SUCCESS);
    }

    @Operation(summary = "그룹 탈퇴", description = "그룹에서 탈퇴합니다.")
    @DeleteMapping("/{groupId}/members")
    public ApiResponseDto<?> leaveGroup(@Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long groupId) {
        groupService.leaveGroup(user.getUsername(), groupId);
        return ApiResponseDto.success(SuccessStatus.LEAVE_GROUP_SUCCESS);
    }


    @Operation(summary = "그룹 내 영수증 조회", description = "그룹 내 영수증을 조회합니다. 회계인 경우 전체 영수증을, 일반 사용자인 경우 자신이 업로드한 영수증을 조회합니다.")
    @GetMapping("/{groupId}/receipts")
    public ApiResponseDto<ReceiptListResponseDto> getReceiptListInGroup(
        @AuthenticationPrincipal User user, @PathVariable Long groupId, @RequestParam(defaultValue = "0") int page) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_SUCCESS, groupService.getReceiptListInGroup(user.getUsername(), groupId, page));
    }

}
