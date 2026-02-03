package com.abdelaziz26.metriplate.utils.ai;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class PromptLoader {
    public String load(String fileName) {

        try (InputStream is =
                     getClass().getClassLoader()
                             .getResourceAsStream("prompts/" + fileName)) {

            if (is == null)
                throw new RuntimeException("Prompt not found");

            return new String(is.readAllBytes(),
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt", e);
        }
    }
}
