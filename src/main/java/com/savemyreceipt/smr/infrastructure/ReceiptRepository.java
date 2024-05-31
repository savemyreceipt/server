package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Receipt;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("select r from Receipt r where r.group = :group")
    Page<Receipt> getReceiptListInGroup(Group group, Pageable pageable);

    Long countByGroup(Group group);

    @Query("select r from Receipt r where r.member = :member AND r.group = :group")
    Page<Receipt> getReceiptListInGroup(@Param("member")Member member, @Param("group")Group group, Pageable pageable);

    default Receipt getReceiptById(Long id) {
        return findById(id).orElseThrow(
            () -> new CustomException(ErrorStatus.RECEIPT_NOT_FOUND,
                ErrorStatus.RECEIPT_NOT_FOUND.getMessage())
        );
    }
}
