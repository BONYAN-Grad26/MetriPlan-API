package com.abdelaziz26.metriplate.services.gemini;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class GeminiService implements AiService {

    private final ChatClient chatClient;

    public GeminiService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    @Override
    public String askAi(String prmpt) {
        return chatClient.prompt(prmpt).call().content();
    }
}
