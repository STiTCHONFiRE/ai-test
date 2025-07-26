package ru.stitchonfire.aitest.dto.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@JacksonXmlRootElement(localName = "yml_catalog")
public record YmlRealStateCatalogDto(
        @JacksonXmlProperty(isAttribute = true, localName = "date")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
        OffsetDateTime date,

        @JacksonXmlProperty(localName = "shop")
        Shop shop
) {
    @Builder
    public record Shop(
            @JacksonXmlProperty(localName = "name")
            String name,

            @JacksonXmlProperty(localName = "company")
            String company,

            @JacksonXmlProperty(localName = "url")
            String url,

            @JacksonXmlProperty(localName = "email")
            String email,

            @JacksonXmlElementWrapper(localName = "currencies")
            @JacksonXmlProperty(localName = "currency")
            List<Currency> currencies,

            @JacksonXmlElementWrapper(localName = "categories")
            @JacksonXmlProperty(localName = "category")
            List<Category> categories,

            @JacksonXmlElementWrapper(localName = "sets")
            @JacksonXmlProperty(localName = "set")
            List<Set> sets,

            @JacksonXmlElementWrapper(localName = "offers")
            @JacksonXmlProperty(localName = "offer")
            List<Offer> offers
    ) {
    }

    @Builder
    public record Currency(
            @JacksonXmlProperty(isAttribute = true, localName = "id")
            String id,

            @JacksonXmlProperty(isAttribute = true, localName = "rate")
            int rate
    ) {
    }

    @Builder
    public record Category(
            @JacksonXmlProperty(isAttribute = true, localName = "id")
            String id,

            @JacksonXmlProperty(isAttribute = true, localName = "parentId")
            String parentId,

            @JacksonXmlText
            String name
    ) {
    }

    @Builder
    public record Set(
            @JacksonXmlProperty(isAttribute = true, localName = "id")
            String id,

            @JacksonXmlProperty(localName = "name")
            String name,

            @JacksonXmlProperty(localName = "url")
            String url
    ) {
    }

    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Offer {
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        String id;

        @JacksonXmlProperty(localName = "name")
        String name;

        @JsonProperty("vendor") // Для обработки опционального поля
        String vendor;

        @JacksonXmlProperty(localName = "url")
        String url;

        @JacksonXmlProperty(localName = "price")
        Price price;

        @JacksonXmlProperty(localName = "currencyId")
        String currencyId;

        @JacksonXmlProperty(localName = "categoryId")
        String categoryId;

        @JacksonXmlProperty(localName = "set-ids")
        String setIds;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "picture")
        List<String> pictures;

        @JsonProperty("description") // Опциональное поле
        String description;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "param")
        List<Param> params;
    }

    @Builder
    public record Price(
            @JacksonXmlProperty(isAttribute = true, localName = "from")
            Boolean from,

            @JacksonXmlText
            String value
    ) {
    }

    @Builder
    public record Param(
            @JacksonXmlProperty(isAttribute = true, localName = "name")
            String name,

            @JacksonXmlProperty(isAttribute = true, localName = "unit")
            String unit,

            @JacksonXmlText
            String value
    ) {
    }
}


