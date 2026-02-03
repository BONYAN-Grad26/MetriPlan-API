package com.abdelaziz26.metriplate.configurations;

import com.google.api.client.util.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${spring.ai.vertex.ai.gemini.model:gemini-1.5-pro}")
    private String modelName;

    @Value("${spring.ai.vertex.ai.gemini.temperature:0.1}")
    private Double temperature;

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        VertexAiGeminiChatOptions options = VertexAiGeminiChatOptions.builder()
                .model(modelName)
                .temperature(temperature)
                .responseMimeType("application/json")
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }
}
