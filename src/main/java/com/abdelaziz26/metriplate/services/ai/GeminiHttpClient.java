package com.abdelaziz26.metriplate.services.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiHttpClient implements LlmClient {

    private final ChatClient chatClient;

    public String generateContent(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}


