package com.savemyreceipt.smr.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class GeminiConfig {

    @Value("${spring.cloud.gcp.gemini.credentials.location}")
    private String keyFileLocation;

    @Value("${spring.cloud.gcp.gemini.project-id}")
    private String projectId;

    private String location = "asia-northeast3";

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream();
        return GoogleCredentials.fromStream(keyFile).createScoped("https://www.googleapis.com/auth/cloud-platform", "https://www.googleapis.com/auth/cloud-platform.read-only");
    }

    @Bean
    public VertexAI vertexAI(GoogleCredentials googleCredentials) throws IOException {
        return new VertexAI.Builder()
            .setCredentials(googleCredentials)
            .setProjectId(projectId)
            .setLocation(location)
            .build();
    }


}
