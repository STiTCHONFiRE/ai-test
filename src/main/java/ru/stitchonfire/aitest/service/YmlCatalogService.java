package ru.stitchonfire.aitest.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.stitchonfire.aitest.dto.AnnouncementDataDto;
import ru.stitchonfire.aitest.dto.AnnouncementRequestDto;
import ru.stitchonfire.aitest.dto.xml.YmlCatalogDto;
import ru.stitchonfire.aitest.repository.AnnouncementRepository;
import ru.stitchonfire.aitest.repository.CategoryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class YmlCatalogService {

    private final ChatClient chatClient;
    private final AnnouncementRepository announcementRepository;
    private final CategoryRepository categoryRepository;

    private final XmlMapper xmlMapper;

    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    public YmlCatalogService(ChatClient chatClient, AnnouncementRepository announcementRepository, CategoryRepository categoryRepository) {
        this.chatClient = chatClient;
        this.announcementRepository = announcementRepository;
        this.categoryRepository = categoryRepository;

        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);

        this.xmlMapper = XmlMapper.builder()
                .addModule(xmlModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .enable(SerializationFeature.INDENT_OUTPUT)   // красиво форматируем
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testRequest() {
        executor.execute(() -> {
            int size = 150; // Количество объявлений для обработки

            var categories = categoryRepository.findAllByIdIn(
                    List.of(87, 88, 90, 91, 104, 120, 121, 122, 123, 124, 125, 126, 127, 133)
            );

            var result = announcementRepository.findByCategory_IdInAndIsDeletedFalseAndStatus_Id(
                    List.of(87, 88, 90, 91, 104, 120, 121, 122, 123, 124, 125, 126, 127, 133),
                    (short) 2
            );

            var categoryWrapper = YmlCatalogDto.CategoryWrapper.builder()
                    .category(
                            categories.stream()
                                    .map(category -> YmlCatalogDto.Category.builder()
                                            .id(category.getId().toString())
                                            .title(category.getName())
                                            .build())
                                    .toList())
                    .build();

            var ymlBuilder = YmlCatalogDto.builder();

            var shopBuilder = YmlCatalogDto.Shop.builder()
                    .name("Bamen")
                    .company("Bamen")
                    .url("https://bamen.ru")
                    .categories(categoryWrapper);

            log.info("Start processing {} announcements", result.size());

            List<YmlCatalogDto.Offer> offers = new ArrayList<>(size);

            int i = 0;
            for (var announcement : result.stream().limit(size).toList()) {
                log.info("Processing announcement with id: {}; {}/{}", announcement.getId(), i + 1, size);
                var res = getAiResponse(new AnnouncementRequestDto(announcement.getTitle(), announcement.getDescription()));

                var offer = YmlCatalogDto.Offer.builder()
                        .id(announcement.getId().toString())
                        .name(res.name())
                        .vendor(res.vendor())
                        .vendorCode(res.vendorCode())
                        .condition(YmlCatalogDto.Condition.builder()
                                .type("preowned")
                                .quality("excellent")
                                .build())
                        .description(announcement.getDescription())
                        .price(announcement.getPrice())
                        .currencyId("RUB")
                        .categoryId(announcement.getCategory().getId())
                        .build();

                offers.add(offer);
                i++;

                try {
                    Thread.sleep(Duration.ofSeconds(3));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            shopBuilder.offers(YmlCatalogDto.OfferWrapper.builder().offer(offers).build());

            var shop = shopBuilder.build();

            var ymlCatalog = ymlBuilder
                    .date(Instant.now().toString())
                    .shop(shop)
                    .build();

            Path outPath = Path.of("output", "yml_catalog.xml");
            try {
                Files.createDirectories(outPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(outPath.toAbsolutePath());

            try (var out = Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                xmlMapper.writeValue(out, ymlCatalog);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

//        System.out.println("Raw response: " + raw);

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
