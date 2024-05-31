package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.receipt.response.ReceiptResponseDto;
import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.GroupMember;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Receipt;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.savemyreceipt.smr.infrastructure.GroupMemberRepository;
import com.savemyreceipt.smr.infrastructure.GroupRepository;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import com.savemyreceipt.smr.infrastructure.ReceiptRepository;
import com.savemyreceipt.smr.utils.DataBucketUtil;
import com.savemyreceipt.smr.utils.GeminiUtil;
import com.savemyreceipt.smr.utils.ReceiptInfo;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final DataBucketUtil dataBucketUtil;
    private final GeminiUtil geminiUtil;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private String IMAGE_URI_PREFIX;
    private String GS_URI_PREFIX;

    @PostConstruct
    private void init() {
        this.IMAGE_URI_PREFIX = "https://storage.googleapis.com/" + bucketName + "/";
        this.GS_URI_PREFIX = "gs://" + bucketName + "/";
    }

    @Transactional
    public ReceiptResponseDto uploadReceipt(String email, MultipartFile file, Long groupId) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        GroupMember groupMember = groupMemberRepository.getGroupMemberByGroupIdAndMemberId(groupId, member.getId());

        log.info("그룹 멤버 조회 완료. groupMember: {}", groupMember);

        try {
            String uuid = dataBucketUtil.uploadImage(file.getBytes(), file.getContentType());
            String imageUri = IMAGE_URI_PREFIX + uuid;
            log.info("이미지 업로드 완료. 주소: {}", imageUri);
            Receipt receipt = Receipt.builder()
                .member(member)
                .group(group)
                .imageUri(imageUri)
                .build();
            log.info("영수증 저장 완료. receipt: {}", receipt);
            ReceiptInfo receiptInfo = geminiUtil.sendReceipt(GS_URI_PREFIX + uuid,
                file.getContentType());
            log.info("이미지 OCR 완료. temp: {}", receiptInfo);
            receipt.updateReceipt(receiptInfo);
            receiptRepository.save(receipt);
            return ReceiptResponseDto.of(receipt);
        } catch (IOException e) {
            throw new CustomException(ErrorStatus.IMAGE_UPLOAD_FAILED, ErrorStatus.IMAGE_UPLOAD_FAILED.getMessage());
        }
    }
}
