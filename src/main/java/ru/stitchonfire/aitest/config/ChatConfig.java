package ru.stitchonfire.aitest.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        String defaultSystemPrompt = """
                Ты — модератор онлайн-магазина. Тебе необходимо анализировать объявления пользователей и решать различные задачи, связанные с обновлением данных на основании информации из объявлений.
                """;

        return builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultSystem(defaultSystemPrompt)
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.1)
                        .build()
                )
                .build();
    }

}
