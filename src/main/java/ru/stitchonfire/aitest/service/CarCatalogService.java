package ru.stitchonfire.aitest.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.stitchonfire.aitest.dto.xml.YmlRealStateCatalogDto;
import ru.stitchonfire.aitest.model.AnnouncementField;
import ru.stitchonfire.aitest.model.AnnouncementResource;
import ru.stitchonfire.aitest.repository.FieldOptionListValueRepository;
import ru.stitchonfire.aitest.repository.LocationRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CarCatalogService {

    LocationRepository locationRepository;
    FieldOptionListValueRepository fieldOptionListValueRepository;

    XmlMapper xmlMapper;

    @Autowired
    public CarCatalogService(LocationRepository locationRepository, FieldOptionListValueRepository fieldOptionListValueRepository) {
        this.locationRepository = locationRepository;
        this.fieldOptionListValueRepository = fieldOptionListValueRepository;

        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);

        this.xmlMapper = XmlMapper.builder()
                .addModule(xmlModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();

        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        xmlMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void createCarCatalog() {
        var result = locationRepository.getLocations(
                81
        ).stream().filter(x -> x.getParent() == null).toList();

        var ymlCatalogBuilder = YmlRealStateCatalogDto.builder();

        var shopBuilder = YmlRealStateCatalogDto.Shop.builder();

        shopBuilder.name("Bamen")
                .url("https://bamen.ru")
                .company("Bamen")
                .email("support@bamen.ru")
                .currencies(
                        Collections.singletonList(
                                YmlRealStateCatalogDto.Currency.builder()
                                        .id("RUR")
                                        .rate(1)
                                        .build()
                        )
                )
                .categories(
                        List.of(
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("81")
                                        .name("Автомобили")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6649")
                                        .name("Седан")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6650")
                                        .name("Хетчбек")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6651")
                                        .name("Универсал")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6652")
                                        .name("Внедорожник")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6653")
                                        .name("Кабриолет")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6654")
                                        .name("Купе")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6655")
                                        .name("Минивэн")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6656")
                                        .name("Пикап")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6657")
                                        .name("Фургон")
                                        .parentId("81")
                                        .build(),
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("6658")
                                        .name("Микроавтобус")
                                        .parentId("81")
                                        .build()
                        )
                );

        List<YmlRealStateCatalogDto.Set> sets = new ArrayList<>();
        List<YmlRealStateCatalogDto.Offer> offers = new ArrayList<>();
        for (var location : result) {
            var firstAnnouncement = location.getAnnouncements().stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            var categoryPath = firstAnnouncement.getCategory().getSeoInfo().get("FriendlyUrl");
            var locationPath = location.getSeoInfo().get("FriendlyUrl");

            String setUrl = "https://bamen.ru/" +
                    locationPath +
                    "/" +
                    categoryPath;

            var setId = UUID.randomUUID();

            sets.add(
                    YmlRealStateCatalogDto.Set.builder()
                            .id(setId.toString())
                            .name("Купить машину " + location.getSeoInfo().get("Title"))
                            .url(setUrl)
                            .build()
            );

            offers.addAll(
                    location.getAnnouncements().stream()
                            .map(announcement -> {
                                var url = announcement.getAvailableSiteUrls().stream().findFirst();

                                var offerBuilder = YmlRealStateCatalogDto.Offer.builder()
                                        .id(announcement.getId().toString())
                                        .currencyId("RUR")
                                        .categoryId(announcement.getCategory().getId().toString())
                                        .price(YmlRealStateCatalogDto.Price.builder()
                                                .value(announcement.getPrice().toString())
                                                .build())
                                        .setIds(setId.toString())
                                        .url(url.get().getUrl());

                                if (announcement.getAnnouncementResources() != null) {
                                    var images = announcement.getAnnouncementResources().stream()
                                            .filter(r -> r.getType().equals("Image"))
                                            .map(AnnouncementResource::getUrl)
                                            .map(uri -> "https://buumba-public.s3.yandexcloud.net/" + uri)
                                            .toList();

                                    offerBuilder.pictures(images);
                                }

                                List<YmlRealStateCatalogDto.Param> params = new ArrayList<>();
                                var fields = announcement.getAnnouncementFields().stream()
                                        .collect(
                                                Collectors.toMap(
                                                        s -> s.getCategoryField().getName(),
                                                        AnnouncementField::getValue
                                                )
                                        );

                                if (fields.containsKey("Год выпуска")) {
                                    params.add(
                                            YmlRealStateCatalogDto.Param.builder()
                                                    .name("Год создания")
                                                    .value(fields.get("Год выпуска"))
                                                    .build()
                                    );
                                }

                                if (fields.containsKey("Коробка передач")) {
                                    var paramBuilder = YmlRealStateCatalogDto.Param.builder()
                                            .name("Коробка передач");

                                    switch (fields.get("Коробка передач")) {
                                        case "6635" -> paramBuilder.value("Механическая");
                                        case "6636" -> paramBuilder.value("Автоматическая");
                                        case "6637" -> paramBuilder.value("Робот");
                                        case "6638" -> paramBuilder.value("Вариатор");
                                    }

                                    params.add(paramBuilder.build());
                                }

                                if (fields.containsKey("Привод")) {
                                    var paramBuilder = YmlRealStateCatalogDto.Param.builder()
                                            .name("Привод");

                                    switch (fields.get("Привод")) {
                                        case "6644" -> paramBuilder.value("Передний");
                                        case "6645" -> paramBuilder.value("Полный");
                                        case "6646" -> paramBuilder.value("Задний");
                                    }

                                    params.add(paramBuilder.build());
                                }

                                if (fields.containsKey("Пробег")) {
                                    params.add(
                                            YmlRealStateCatalogDto.Param.builder()
                                                    .name("Пробег")
                                                    .value(fields.get("Пробег"))
                                                    .build()
                                    );
                                }

                                if (fields.containsKey("Мощность двигателя, л.с.")) {
                                    params.add(
                                            YmlRealStateCatalogDto.Param.builder()
                                                    .name("Двигатель, л.с.")
                                                    .value(fields.get("Мощность двигателя, л.с."))
                                                    .build()
                                    );
                                }

                                if (fields.containsKey("Тип двигателя")) {
                                    var paramBuilder = YmlRealStateCatalogDto.Param.builder()
                                            .name("Топливо");

                                    var type = switch (fields.get("Тип двигателя")) {
                                        case "6639" -> "Бензин";
                                        case "6640" -> "Дизель";
                                        case "6642" -> "Гибрид";
                                        case "6643" -> "Электро";
                                        default -> null;
                                    };

                                    if (type != null) {
                                        params.add(paramBuilder.value(type).build());
                                    }
                                }

                                if (fields.containsKey("Марка")) {
                                    fieldOptionListValueRepository.findById(Integer.parseInt(fields.get("Марка")))
                                            .ifPresent(value -> {
                                                offerBuilder.vendor(value.getName());
                                            });
                                }

                                if (fields.containsKey("Модель") && fields.containsKey("Марка")) {
                                    fieldOptionListValueRepository.findById(Integer.parseInt(fields.get("Модель")))
                                            .ifPresent(model -> {
                                                fieldOptionListValueRepository.findById(Integer.parseInt(fields.get("Марка")))
                                                        .ifPresent(mark -> {
                                                            offerBuilder.name(mark.getName() + " " + model.getName());
                                                        });
                                            });
                                }

                                params.add(
                                        YmlRealStateCatalogDto.Param.builder()
                                                .name("Конверсия")
                                                .value(String.valueOf(announcement.getNumberOfViews()))
                                                .build()
                                );

                                offerBuilder.params(params);

                                if (fields.containsKey("Тип кузова")) {
                                    if (!fields.get("Тип кузова").equals("6659")) {
                                        offerBuilder.categoryId(fields.get("Тип кузова"));
                                    } else {
                                        offerBuilder.categoryId("81");
                                    }
                                } else {
                                    offerBuilder.categoryId("81");
                                }

                                return offerBuilder.build();
                            })
                            .toList()
            );
        }

        shopBuilder.offers(offers)
                .sets(sets);

        ymlCatalogBuilder
                .date(OffsetDateTime.now(ZoneOffset.ofHours(3)))
                .shop(shopBuilder.build());

        Path outPath = Path.of("output", "yml_car_catalog.xml");
        try {
            Files.createDirectories(outPath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(outPath.toAbsolutePath());

        try (var out = Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            xmlMapper.writeValue(out, ymlCatalogBuilder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
