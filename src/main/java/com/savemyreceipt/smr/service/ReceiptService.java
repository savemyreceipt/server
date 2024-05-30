package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.infrastructure.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
}
