package com.savemyreceipt.smr.DTO.notification;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponseDto {

    List<NotificationResponseDto> notificationList;
}
