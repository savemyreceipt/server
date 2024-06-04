package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.DTO.notification.NotificationListResponseDto;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Notification", description = "알림 관리 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponseDto<NotificationListResponseDto> getNotificationList(
        @AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page) {
        return ApiResponseDto.success(SuccessStatus.GET_NOTIFICATION_LIST_SUCCESS, notificationService.getNotificationList(user.getUsername(), page));
    }

    @PutMapping("/{notificationId}")
    public ApiResponseDto<?> checkNotification(
        @AuthenticationPrincipal User user, @PathVariable Long notificationId) {
        notificationService.checkNotification(user.getUsername(), notificationId);
        return ApiResponseDto.success(SuccessStatus.CHECK_NOTIFICATION_SUCCESS);
    }

}
