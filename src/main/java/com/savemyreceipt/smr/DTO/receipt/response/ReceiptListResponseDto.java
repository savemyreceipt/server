package com.savemyreceipt.smr.DTO.receipt.response;

import java.util.List;
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
