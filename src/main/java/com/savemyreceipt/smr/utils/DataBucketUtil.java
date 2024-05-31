package com.savemyreceipt.smr.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataBucketUtil {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public String uploadImage(byte[] file, String contentType) throws IOException {

        // 이미지 uuid와 파일 형식
        String uuid = UUID.randomUUID().toString();
        log.info("uuid: {}", uuid);

        // GCS에 이미지 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
            .setContentType(contentType)
            .build();
        storage.create(blobInfo, file);
        return uuid;
    }

    public void deleteImage(String uuid) {
        Blob blob = storage.get(bucketName, uuid);
        if (blob == null ) {
            return;
        }
        log.info("blob: {}", blob);
        Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());
        storage.delete(bucketName, uuid, precondition);
    }


}
