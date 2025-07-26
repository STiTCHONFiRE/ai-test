package ru.stitchonfire.aitest.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        String defaultSystemPrompt = """
                Ты модератор, который создает дополнительное представление об объявлении на основании информации, предоставленной пользователем.
                Все дело в том что объявление может быть не полным, и нужно заполнить недостающие поля.
                
                Итак на вход ты получаешь 3 значения о местоположении: это город и район города, в котором находится объявление, а также тип: покупка квартиры, посуточная аренда или долгосрочная аренда.
                
                Твоя задача - создать представление об объявлении, которое будет содержать следующие поля:
                - name: название представления, которое должно быть составлено
                """;

        return builder
                .defaultSystem(defaultSystemPrompt)
                .defaultOptions(
                        OllamaOptions.builder()
                                .temperature(0.1)
                                .topK(2)
                                .topP(1.0)
                                .minP(0.1)
                                .frequencyPenalty(0.2)
                                .build()
                ).build();
    }

}
