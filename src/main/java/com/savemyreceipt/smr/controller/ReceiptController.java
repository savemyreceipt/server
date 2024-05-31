package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.service.ReceiptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Receipt", description = "영수증 관리 API")
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/upload/{groupId}")
    public ApiResponseDto<?> uploadReceipt(@AuthenticationPrincipal User user,
        @RequestPart MultipartFile file,
        @PathVariable Long groupId) {
        if (file.isEmpty() || file.getContentType() == null) {
            return ApiResponseDto.error(ErrorStatus.IMAGE_NOT_FOUND);
        }
        return ApiResponseDto.success(SuccessStatus.UPLOAD_RECEIPT_SUCCESS,         receiptService.uploadReceipt(user.getUsername(), file, groupId));
    }
}
