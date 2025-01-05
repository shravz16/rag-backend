package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class EmbeddedService {
    private final OkHttpClient client = new OkHttpClient();
    private final String HUGGINGFACE_API_KEY = "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";
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/intfloat/multilingual-e5-large";

    public List<Double> generateEmbedding(String text) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonBody = String.format("{\"inputs\": \"%s\"}", text);

        Request request = new Request.Builder()
                .url(MODEL_URL)
                .addHeader("Authorization", "Bearer " + HUGGINGFACE_API_KEY)
                .post(RequestBody.create(JSON, jsonBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }



            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            // Parse as Double array directly since we get a flat array
            Double[] embedding = mapper.readValue(responseBody, Double[].class);
            return Arrays.asList(embedding);

        }
        }

}
