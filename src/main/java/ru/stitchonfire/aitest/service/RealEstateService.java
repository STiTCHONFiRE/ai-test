package ru.stitchonfire.aitest.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stitchonfire.aitest.dto.xml.YmlRealStateCatalogDto;
import ru.stitchonfire.aitest.model.AnnouncementField;
import ru.stitchonfire.aitest.model.AnnouncementResource;
import ru.stitchonfire.aitest.model.Location;
import ru.stitchonfire.aitest.repository.LocationRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RealEstateService {

    LocationRepository locationRepository;
    XmlMapper xmlMapper;

    @Autowired
    public RealEstateService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;

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
//    @EventListener(ApplicationReadyEvent.class)
    public void createYmlCatalog() {
        var result = locationRepository.getLocationsWithFilteredDate(
                93,
                LocalDateTime.now().minusMonths(4).toInstant(ZoneOffset.of("+03:00"))
        );

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
                );

        List<YmlRealStateCatalogDto.Set> sets = new ArrayList<>();
        Map<Long, YmlRealStateCatalogDto.Offer> offerMap = new HashMap<>();
        List<YmlRealStateCatalogDto.Offer> offers = new ArrayList<>();

        for (var location : result) {
            var groupedAnnouncements = location.getAnnouncements().stream()
                    .collect(groupingBy(announcement -> {
                        var fields = announcement.getAnnouncementFields().stream()
                                .collect(
                                        Collectors.toMap(
                                                s -> s.getCategoryField().getName(),
                                                AnnouncementField::getValue
                                        )
                                );

                        return fields.get("Выбор");
                    }));

            var firstAnnouncement = location.getAnnouncements().stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            var categoryPath = firstAnnouncement.getCategory().getSeoInfo().get("FriendlyUrl");
            var parentCategoryPath = firstAnnouncement.getCategory().getParent().getSeoInfo().get("FriendlyUrl");
            var locationPath = location.getParent() == null ? location.getSeoInfo().get("FriendlyUrl") : location.getParent().getSeoInfo().get("FriendlyUrl");

            String setUrl = "https://bamen.ru/" +
                    locationPath +
                    "/" +
                    parentCategoryPath +
                    "/" +
                    categoryPath;

            YmlRealStateCatalogDto.Set sellSet;
            YmlRealStateCatalogDto.Set dailySet;
            YmlRealStateCatalogDto.Set allSet;

            if (groupedAnnouncements.get("7467") != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Купить квартиру ");
                sellSet = createSet(location, setUrl, sets, stringBuilder);
            } else {
                sellSet = null;
            }

            if (groupedAnnouncements.get("7468") != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Посуточная аренда квартиры ");
                dailySet = createSet(location, setUrl, sets, stringBuilder);
            } else {
                dailySet = null;
            }

            if (groupedAnnouncements.get("7469") != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Аренда квартиры ");
                allSet = createSet(location, setUrl, sets, stringBuilder);
            } else {
                allSet = null;
            }

            groupedAnnouncements.forEach((key, value) -> {
                value.forEach(announcement -> {
                    if (announcement.getPrice() == null) {
                        return;
                    }

                    if (!(key.equals("7467") || key.equals("7468") || key.equals("7469"))) {
                        return;
                    }

                    if (offerMap.containsKey(announcement.getId())) {
                        var offer = offerMap.get(announcement.getId());
                        if (key.equals("7468")) {
                            if (dailySet != null) {
                                offer.setSetIds(offer.getSetIds() + ", " + dailySet.id());
                            }

                            if (allSet != null) {
                                offer.setSetIds(offer.getSetIds() + ", " + allSet.id());
                            }
                        }

                        if (key.equals("7469")) {
                            if (allSet != null) {
                                offer.setSetIds(offer.getSetIds() + ", " + allSet.id());
                            }
                        }

                        return;
                    }

                    var url = announcement.getAvailableSiteUrls().stream().findFirst();

                    if (url.isEmpty()) {
                        log.warn("Announcement with id {} has no available site URL", announcement.getId());
                        return;
                    }

                    var offerBuilder = YmlRealStateCatalogDto.Offer.builder()
                            .id(announcement.getId().toString())
                            .name(announcement.getTitle())
                            .currencyId("RUR")
                            .categoryId(announcement.getCategory().getId().toString())
                            .price(YmlRealStateCatalogDto.Price.builder()
                                    .value(announcement.getPrice().toString())
                                    .build())
                            .url(url.get().getUrl());

                    List<String> setIds = new ArrayList<>();

                    if (key.equals("7467")) {
                        if (sellSet != null) {
                            setIds.add(sellSet.id());
                        }
                    }

                    if (key.equals("7468")) {
                        if (dailySet != null) {
                            setIds.add(dailySet.id());
                        }

                        if (allSet != null) {
                            setIds.add(allSet.id());
                        }
                    }

                    if (key.equals("7469")) {
                        if (allSet != null) {
                            setIds.add(allSet.id());
                        }
                    }

                    offerBuilder.setIds(String.join(", ", setIds));

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

                    fields.forEach((fieldName, fieldValue) -> {
                        if (fieldName.equals("Общая площадь, кв.м.")) {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Площадь")
                                    .value(fieldValue)
                                    .build());
                        }

                        if (fieldName.equals("Количество комнат")) {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Число комнат")
                                    .value(fieldValue)
                                    .build());
                        }

                        if (fieldName.equals("Этажей в доме")) {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Число этажей")
                                    .value(fieldValue)
                                    .build());
                        }

                        if (fieldName.equals("Этаж")) {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Этаж")
                                    .value(fieldValue)
                                    .build());
                        }
                    });

                    if (key.equals("7467")) {
                        params.add(YmlRealStateCatalogDto.Param.builder()
                                .name("Тип предложения")
                                .value("Продажа")
                                .build());
                    }

                    if (key.equals("7468") || key.equals("7469")) {
                        params.add(YmlRealStateCatalogDto.Param.builder()
                                .name("Тип предложения")
                                .value("Аренда")
                                .build());

                        if (key.equals("7468")) {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Посуточно")
                                    .value("true")
                                    .build());
                        } else {
                            params.add(YmlRealStateCatalogDto.Param.builder()
                                    .name("Посуточно")
                                    .value("false")
                                    .build());
                        }
                    }

                    params.add(YmlRealStateCatalogDto.Param.builder()
                            .name("Конверсия")
                            .value(String.valueOf(announcement.getNumberOfViews()))
                            .build());

                    offerBuilder.params(params);
                    var offer = offerBuilder.build();

                    offers.add(offer);
                    offerMap.put(announcement.getId(), offer);
                });
            });
        }

        shopBuilder
                .sets(sets)
                .offers(offers)
                .categories(
                        Collections.singletonList(
                                YmlRealStateCatalogDto.Category.builder()
                                        .id("93")
                                        .name("Квартиры")
                                        .build()
                        )
                );

        System.out.println(offers.size());

        ymlCatalogBuilder
                .date(OffsetDateTime.now(ZoneOffset.ofHours(3)))
                .shop(shopBuilder.build());
        Path outPath = Path.of("output", "yml_real_state_catalog.xml");
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

    private YmlRealStateCatalogDto.Set createSet(Location location, String setUrl, List<YmlRealStateCatalogDto.Set> sets, StringBuilder stringBuilder) {
        if (location.getParent() != null) {
            stringBuilder.append(location.getParent().getSeoInfo().get("Title"))
                    .append(", ");

            stringBuilder.append(location.getName());
        } else {
            stringBuilder.append(location.getSeoInfo().get("Title"));
        }

        var setId = UUID.randomUUID();

        var set = YmlRealStateCatalogDto.Set.builder()
                .id(setId.toString())
                .name(stringBuilder.toString())
                .url(setUrl)
                .build();

        sets.add(set);
        return set;
    }

}
