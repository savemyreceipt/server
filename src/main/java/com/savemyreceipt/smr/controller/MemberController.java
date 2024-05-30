package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.DTO.member.request.MemberRequestDto;
import com.savemyreceipt.smr.DTO.member.response.MemberResponseDto;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Member", description = "사용자 관리 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 조회", description = "사용자 정보를 조회합니다.")
    @GetMapping
    public ApiResponseDto<MemberResponseDto> getMember(@Parameter(hidden = true) @AuthenticationPrincipal
        User user) {
        return ApiResponseDto.success(
            SuccessStatus.GET_MEMBER_SUCCESS, memberService.getMember(user.getUsername()));
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @PutMapping
    public ApiResponseDto<?> updateMember(@Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestBody MemberRequestDto memberRequestDto) {
        memberService.updateMember(user.getUsername(), memberRequestDto);
        return ApiResponseDto.success(SuccessStatus.UPDATE_MEMBER_SUCCESS);
    }

}
