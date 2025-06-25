package ru.stitchonfire.aitest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.stitchonfire.aitest.dto.AnnouncementRequestDto;
import ru.stitchonfire.aitest.dto.AnnouncementDataDto;
import ru.stitchonfire.aitest.repository.AnnouncementRepository;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class YmlCatalogService {

    ChatClient chatClient;
    AnnouncementRepository announcementRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void testRequest() {
        var result = announcementRepository.findByCategory_IdInAndIsDeletedFalseAndStatus_Id(
                List.of(87, 88, 90, 91, 104, 120, 121, 122, 123, 124, 125, 126, 127, 133),
                (short) 2
        );

        Thread.ofVirtual().start(() ->
                result.stream().limit(3).forEach(announcement -> {
                    var res = getAiResponse(new AnnouncementRequestDto(announcement.getTitle(), announcement.getDescription()));
                    System.out.println("AI Response: " + res.toString() + "\n" + "name: " + announcement.getTitle());
                    try {
                        Thread.sleep(Duration.ofSeconds(10));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    public AnnouncementDataDto getAiResponse(AnnouncementRequestDto dto) {
        String raw = chatClient.prompt("""
                         Данные которые необходимо обработать разбора:
                         ```
                         name: %s
                         description: %s
                         ```
                        """.formatted(dto.name(), dto.description()))
                .call()
                .content();

        System.out.println("Raw response: " + raw);

        try {
            assert raw != null;
            return extractJson(raw, AnnouncementDataDto.class, new ObjectMapper());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T extractJson(String raw, Class<T> clazz, ObjectMapper mapper) throws IOException {
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start == -1 || end == -1 || start >= end) {
            throw new IOException("Valid JSON object not found in LLM output");
        }
        String json = raw.substring(start, end + 1);
        return mapper.readValue(json, clazz);
    }
}
