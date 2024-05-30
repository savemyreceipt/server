package com.savemyreceipt.smr.DTO.receipt.response;

import com.savemyreceipt.smr.domain.Receipt;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptResponseDto {

    private Long id;
    private String imageUri;
    private String category;
    private String description;
    private LocalDate purchaseDate;
    private Long price;
    private boolean isChecked;

    public static ReceiptResponseDto of(Receipt receipt) {
        return ReceiptResponseDto.builder()
            .id(receipt.getId())
            .imageUri(receipt.getImageUri())
            .category(receipt.getCategory())
            .description(receipt.getDescription())
            .purchaseDate(receipt.getPurchasedAt())
            .price(receipt.getPrice())
            .isChecked(receipt.isChecked())
            .build();
    }
}
