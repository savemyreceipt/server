package com.savemyreceipt.smr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiUtil {

    private final VertexAI vertexAI;

    public ReceiptInfo sendReceipt(String imageUri, String mimeType) throws IOException {
        try {

            log.info("imageUri: {}", imageUri);
            log.info("mimeType: {}", mimeType);

            String modelName = "gemini-1.5-flash-001";
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(
                ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData(mimeType, imageUri),
                    "Extract the purchase date and total price from this receipt. Using this JSON schema: Receipt = {\"purchase_date\": str, \"total_price\": Long}. The purchase date should be in the format yyyy-MM-dd, and the price should contain only numbers. If you cannot figure out the exact date or price, then just leave it as null."
                ));
            log.info("response: {}", response);
            String json = response.getCandidates(0).getContent().getParts(0).getText();
            log.info("json: {}", json);
            return jsonParse(json);
        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류 발생", e);
            throw new IOException("Gemini API 호출 중 오류 발생", e);
        }
    }

    private ReceiptInfo jsonParse(String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String cleanedJson = json
            .replace("```json", "") // 시작 부분의 ```json 제거
            .replace("```", "") // 마지막 부분의 ``` 제거
            .trim(); // 앞뒤 공백 제거
        try {
            return objectMapper.readValue(cleanedJson, ReceiptInfo.class);
        } catch (IOException e) {
            log.error("ReceiptInfo 객체 생성 중 오류 발생", e);
            throw new CustomException(ErrorStatus.IMAGE_PROCESS_FAILED,
                ErrorStatus.IMAGE_PROCESS_FAILED.getMessage());
        }
    }
}
