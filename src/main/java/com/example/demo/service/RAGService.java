package com.example.demo.service;



import com.google.protobuf.Struct;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.configs.PineconeConfig;
import io.pinecone.configs.PineconeConnection;
import io.pinecone.proto.QueryRequest;
import io.pinecone.proto.QueryResponse;
import io.pinecone.proto.ScoredVector;
import io.pinecone.proto.VectorServiceGrpc;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import io.pinecone.unsigned_indices_model.ScoredVectorWithUnsignedIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RAGService {


    private final OpenAiService openAiService;
    private final String indexName;
    private final String namespace;
    private final EmbeddedService embeddingService;

    @Autowired
    private final Index index;

    public RAGService(

            OpenAiService openAiService,
            EmbeddedService embeddingService,
            @Value("${pinecone.index.name}") String indexName,
            @Value("${pinecone.namespace}") String namespace,
            Index index
            ) {

        this.openAiService = openAiService;
        this.indexName = indexName;
        this.namespace = namespace;
        this.embeddingService=embeddingService;
        this.index=index;
    }

    public String processQuery(String query,Long customerId) {
        try {
            // 1. Generate embedding for the query
            List<Double> queryEmbedding = generateEmbedding(query);

            // 2. Search similar vectors in Pinecone
            List<ScoredVectorWithUnsignedIndices> similarVectors = searchSimilarVectors(queryEmbedding,customerId);

            // 3. Extract context from similar vectors
            String context = extractContext(similarVectors);

            // 4. Generate response using OpenAI
            return generateResponse(query, context);

        } catch (Exception e) {

            throw new RuntimeException("Failed to process query", e);
        }
    }

    private List<Double> generateEmbedding(String text) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Collections.singletonList(text))
                .build();

        List<Embedding> embeddings = openAiService.createEmbeddings(request).getData();
        return embeddings.get(0).getEmbedding();
    }

    private List<ScoredVectorWithUnsignedIndices> searchSimilarVectors(List<Double> queryVector,Long customerId) {
        // Convert Double list to float list for Pinecone
        List<Float> floatVector = queryVector.stream()
                .map(Double::floatValue)
                .collect(Collectors.toList());

        PineconeConfig config = new PineconeConfig("1f784203-27de-487e-90be-b2bff7b3b8f8");

        config.setHost("https://docs-rag-chatbot-xcklusr.svc.aped-4627-b74a.pinecone.io");
        PineconeConnection connection = new PineconeConnection(config);
        Index index = new Index(connection, "docs-rag-chatbot");

        QueryResponseWithUnsignedIndices queryResponse = index.query(20, floatVector, null, null, null, "plain-docx-namespace-"+customerId, null, true, true);

        return queryResponse.getMatchesList();
    }

    private String extractContext(List<ScoredVectorWithUnsignedIndices> vectors) {
        StringBuilder context = new StringBuilder();
        for (ScoredVectorWithUnsignedIndices vector : vectors) {    Struct metadata = vector.getMetadata();
            if (metadata != null) {
                com.google.protobuf.Value sourceValue = metadata.getFieldsMap().get("source");
                com.google.protobuf.Value textValue = metadata.getFieldsMap().get("text");

                if (sourceValue != null) {
                    context.append("Source: ").append(sourceValue.getStringValue()).append("\n");
                }
                if (textValue != null) {
                    context.append(textValue.getStringValue()).append("\n\n");
                }
            }
        }
        return context.toString();
    }

    private String generateResponse(String query, String context) {
        String prompt = String.format(
                "Here is information retrieved based on relevance scores:\n\n%s\n\n" +
                        "Based on this information, please answer the question: %s\n\n" +
                        "If the context doesn't provide enough information to answer the question, " +
                        "please say so clearly.",
                context,
                query
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4")
                .messages(Arrays.asList(
                        new ChatMessage("system",
                                "You are a helpful assistant that answers questions based on the provided context. " +
                                        "You should consider the relevance scores when forming your answer. " +
                                        "If you cannot find sufficient information in the context, clearly state that."),
                        new ChatMessage("user", prompt)
                ))
                .maxTokens(500)
                .temperature(0.7)
                .build();

        ChatCompletionResult response = openAiService.createChatCompletion(request);
        return response.getChoices().get(0).getMessage().getContent();
    }}
