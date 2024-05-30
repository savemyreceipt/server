package com.savemyreceipt.smr.utils;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GeminiUtil {

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    private final String location = "us-central1";
    private final String modelName = "gemini-1.5-flash-latest";

    public String sendReceipt(byte[] file) throws IOException {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            String imageUri = "gs://cloud-samples-data/vision/ocr/sign.jpg";

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(
                ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData("image/png", imageUri),
                    "What's in this photo"
                ));
            return response.toString();
        }
    }
}
