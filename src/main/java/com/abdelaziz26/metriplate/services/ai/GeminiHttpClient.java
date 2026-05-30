package com.abdelaziz26.metriplate.services.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiHttpClient implements LlmClient {

    @Value("${spring.ai.google.genai.api-key:}")
    private String apiKey;

    @Value("${spring.ai.model.chat:models/gemini-1.5-pro}")
    private String modelName;

    private final RestTemplate rest = new RestTemplate();

    @Override
    public String generateContent(String prompt) throws Exception{
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Gemini API key is not configured (gemini.api.key)");
        }

        // Normalize modelName. If user sets a shorthand like "google-genai" in application.yml
        // map it to a sensible default model id for Vertex AI.
        String modelPath;
        if (modelName == null || modelName.isBlank()) {
            modelPath = "models/gemini-1.5-pro";
        } else if ("google-genai".equalsIgnoreCase(modelName.trim())) {
            modelPath = "models/gemini-1.5-pro";
        } else {
            modelPath = modelName.startsWith("models/") ? modelName : "models/" + modelName;
        }
        String url = String.format("https://generativelanguage.googleapis.com/v1beta/%s:generateText?key=%s", modelPath, apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> promptObj = new HashMap<>();
        promptObj.put("text", prompt);

        Map<String, Object> request = new HashMap<>();
        request.put("prompt", promptObj);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = rest.postForObject(url, entity, Map.class);
        if (response == null) throw new RuntimeException("Empty response from Gemini API");

        // Try several common response shapes
        // 1) response.output.text
        Object output = response.get("output");
        if (output instanceof Map) {
            Object text = ((Map<String, Object>) output).get("text");
            if (text instanceof String) return (String) text;
        }

        // 2) response.candidates -> list[0].output
        Object candidates = response.get("candidates");
        if (candidates instanceof Iterable) {
            for (Object c : (Iterable) candidates) {
                if (c instanceof Map) {
                    Object o = ((Map<String, Object>) c).get("output");
                    if (o instanceof String) return (String) o;
                    if (o instanceof Map) {
                        Object t = ((Map<String, Object>) o).get("text");
                        if (t instanceof String) return (String) t;
                    }
                }
            }
        }

        // 3) response.response or top-level content
        Object respText = response.get("text");
        if (respText instanceof String) return (String) respText;

        // Fallback: return the JSON as string
        return response.toString();
    }
}


