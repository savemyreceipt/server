package com.savemyreceipt.smr.DTO.notification;

import com.savemyreceipt.smr.domain.Notification;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    public NotificationResponseDto(Long id, String title, String message, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .createdAt(notification.getCreatedAt())
            .build();
    }

}
