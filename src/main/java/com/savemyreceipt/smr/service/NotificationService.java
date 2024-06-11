package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.notification.NotificationListResponseDto;
import com.savemyreceipt.smr.DTO.notification.NotificationResponseDto;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Notification;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import com.savemyreceipt.smr.infrastructure.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotificationList(String username, int page) {
        Member member = memberRepository.getMemberByEmail(username);
        Pageable pageable = PageRequest.of(page, 10);

        Page<Notification> notifications = notificationRepository.findByMemberAndCheckedFalseOrderByCreatedAtDesc(member, pageable);
        Page<NotificationResponseDto> notificationResponseDtos = notifications.map(NotificationResponseDto::of);
        return new NotificationListResponseDto(notificationResponseDtos);
    }

    @Transactional
    public void createNotification(Member member, String title, String message) {
        Notification notification = Notification.builder()
            .title(title)
            .message(message)
            .member(member)
            .build();
        notificationRepository.save(notification);
    }

    @Transactional
    public void checkNotification(String username, Long notificationId) {
        Member member = memberRepository.getMemberByEmail(username);
        Notification notification = notificationRepository.getNotificationById(notificationId);
        if (!notification.getMember().equals(member)) {
            throw new CustomException(ErrorStatus.UNAUTHORIZED_MEMBER_ACCESS, ErrorStatus.UNAUTHORIZED_MEMBER_ACCESS.getMessage());
        }
        notification.check();
        notificationRepository.save(notification);
    }
}
