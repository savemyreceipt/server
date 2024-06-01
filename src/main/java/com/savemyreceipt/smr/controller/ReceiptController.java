package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.DTO.receipt.request.ReceiptUpdateRequestDto;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "영수증 업로드", description = "영수증을 업로드하여 서버에 저장하고 분석합니다.")
    @PostMapping("/upload/{groupId}")
    public ApiResponseDto<?> uploadReceipt(@AuthenticationPrincipal User user,
        @RequestPart MultipartFile file,
        @PathVariable Long groupId) {
        if (file.isEmpty() || file.getContentType() == null) {
            return ApiResponseDto.error(ErrorStatus.IMAGE_NOT_FOUND);
        }
        return ApiResponseDto.success(SuccessStatus.UPLOAD_RECEIPT_SUCCESS,         receiptService.uploadReceipt(user.getUsername(), file, groupId));
    }

    @Operation(summary = "영수증 업데이트 및 확인", description = "영수증을 업데이트하고 확인합니다.")
    @PutMapping("/update/{receiptId}")
    public ApiResponseDto<?> updateReceipt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long receiptId,
        @RequestBody ReceiptUpdateRequestDto receiptUpdateRequestDto) throws IOException {
        receiptService.updateReceipt(user.getUsername(), receiptId, receiptUpdateRequestDto);
        return ApiResponseDto.success(SuccessStatus.UPDATE_RECEIPT_SUCCESS);
    }

    @Operation(summary = "영수증 삭제", description = "영수증을 삭제합니다.")
    @DeleteMapping("/{receiptId}")
    public ApiResponseDto<?> deleteReceipt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long receiptId) {
        receiptService.deleteReceipt(user.getUsername(), receiptId);
        return ApiResponseDto.success(SuccessStatus.DELETE_RECEIPT_SUCCESS);
    }

    @Operation(summary = "영수증 상세 조회", description = "영수증 상세 정보를 조회합니다.")
    @GetMapping("/detail/{receiptId}")
    public ApiResponseDto<?> getReceiptInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long receiptId) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_SUCCESS, receiptService.getReceipt(user.getUsername(), receiptId));
    }
}
