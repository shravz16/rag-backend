package com.example.demo.configuration;

import com.theokanning.openai.service.OpenAiService;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
//import io.pinecone.PineconeClient;
//import io.pinecone.PineconeClientConfig;
//import io.pinecone.PineconeConnection;
//import io.pinecone.PineconeConnectionConfig;

import io.pinecone.clients.Index;
import io.pinecone.configs.PineconeConfig;
import io.pinecone.configs.PineconeConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration

public class RagConfig {
    @Value("${pinecone.api.key}")
    private String pineconeApiKey;

    @Value("${pinecone.environment}")
    private String pineconeEnvironment;

    @Value("${pinecone.index.name}")
    private String pineconeIndexName;

    @Value("${pinecone.namespace}")
    private String pineconeNamespace;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${pinecone.project.name:Default}")
    private String projectName;

    private static final String PINECONE_HOST = "docs-rag-chatbot-xcklusr.svc.aped-4627-b74a.pinecone.io:443";
    private PineconeConfig config ;
    private Index index ;
//    @Bean
//    public PineconeClientConfig pineconeClientConfig() {
//        return new PineconeClientConfig()
//                .withApiKey(pineconeApiKey);
//    }
//
//    @Bean
//    public PineconeConnectionConfig pineconeConnectionConfig() {
//        //log.info("Connecting to Pinecone host: {}", PINECONE_HOST);
//
//        return new PineconeConnectionConfig()
//                .withIndexName(pineconeIndexName)
//                .withCustomChannelBuilder((clientConfig, connConfig) -> {
//                    try {
//                        SslContext sslContext = GrpcSslContexts.forClient()
//                                .trustManager(io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE)
//                                .build();
//
//                        return NettyChannelBuilder
//                                .forTarget(PINECONE_HOST)
//                                .keepAliveWithoutCalls(true)
//                                .keepAliveTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
//                                .negotiationType(NegotiationType.TLS)
//                                .sslContext(sslContext)
//                                .maxInboundMessageSize(64 * 1024 * 1024)
//                                .enableRetry()
//                                .maxRetryAttempts(3)
//                                .build();
//                    } catch (Exception e) {
//                        System.out.println("Failed to create gRPC channel"+ e);
//                        throw new RuntimeException("Failed to create gRPC channel", e);
//                    }
//                });
//    }
//
//    @Bean
//    public PineconeConnection pineconeConnection(
//            PineconeClientConfig clientConfig,
//            PineconeConnectionConfig connectionConfig) {
//        System.out.println("Initializing Pinecone connection for index: "+ pineconeIndexName);
//        try {
//            return new PineconeConnection(clientConfig, connectionConfig);
//        } catch (Exception e) {
//            System.out.println("Failed to create Pinecone connection"+ e);
//            throw e;
//        }
//    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(openaiApiKey);
    }

    @Bean
    public Index index() {
        PineconeConfig config = new PineconeConfig("1f784203-27de-487e-90be-b2bff7b3b8f8");
        config.setHost("https://docs-rag-chatbot-xcklusr.svc.aped-4627-b74a.pinecone.io");
        PineconeConnection connection = new PineconeConnection(config);
        index = new Index(connection, "docs-rag-chatbot");
        return index;
    }

}
