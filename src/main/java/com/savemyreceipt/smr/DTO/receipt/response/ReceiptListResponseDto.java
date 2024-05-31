package com.savemyreceipt.smr.DTO.receipt.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptListResponseDto {

    private Page<ReceiptResponseDto> receiptList;

}
