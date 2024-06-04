package com.savemyreceipt.smr.DTO.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponseDto {

    Page<NotificationResponseDto> notificationList;
}
