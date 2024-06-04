package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Notification;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberAndCheckedFalse(Member member);

    Page<Notification> findByMemberAndCheckedFalse(Member member, Pageable pageable);

    Optional<Notification> findById(Long id);

    default Notification getNotificationById(Long id) {
        return findById(id).orElseThrow(
            () -> new CustomException(ErrorStatus.NOTIFICATION_NOT_FOUND, ErrorStatus.NOTIFICATION_NOT_FOUND.getMessage())
        );
    }

}
