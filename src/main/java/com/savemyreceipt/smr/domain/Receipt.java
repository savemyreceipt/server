package com.savemyreceipt.smr.domain;

import com.savemyreceipt.smr.DTO.receipt.request.ReceiptUpdateRequestDto;
import com.savemyreceipt.smr.utils.ReceiptInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@NoArgsConstructor
@SoftDelete
public class Receipt extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUri;

    private String category;

    private String description;

    private String memo;

    private LocalDate purchasedAt;

    private Long price;

    private boolean isChecked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Receipt(String imageUri, String category, String description, String memo, LocalDate purchasedAt, Long price,
                   Member member, Group group) {
        this.imageUri = imageUri;
        this.category = category;
        this.description = description;
        this.memo = memo;
        this.purchasedAt = purchasedAt;
        this.price = price;
        this.member = member;
        this.group = group;
    }

    private void check() {
        this.isChecked = true;
    }

    public void updateReceipt(ReceiptInfo receiptInfo) {
        this.purchasedAt = receiptInfo.getPurchaseDate();
        this.price = receiptInfo.getTotalPrice();
        this.category = receiptInfo.getCategory();
    }

    public void updateReceipt(ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        this.category = receiptUpdateRequestDto.getCategory();
        this.description = receiptUpdateRequestDto.getDescription();
        this.memo = receiptUpdateRequestDto.getMemo();
        this.purchasedAt = receiptUpdateRequestDto.getPurchaseDate();
        this.price = receiptUpdateRequestDto.getPrice();
        check();
    }

}
