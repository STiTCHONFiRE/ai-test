package ru.stitchonfire.aitest.dto.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Builder;

import java.util.List;


@Builder
@JacksonXmlRootElement(localName = "yml_catalog")
@JsonPropertyOrder({"shop"})
public record YmlCatalogDto(
        @JacksonXmlProperty(isAttribute = true)
        String date,
        Shop shop
) {
    @Builder
    @JsonPropertyOrder({"name", "company", "url", "platform",
            "categories", "deliveryOptions", "offers"})
    public record Shop(
            String name,
            String company,
            String url,
            String platform,

            @JacksonXmlProperty(localName = "categories")
            CategoryWrapper categories,

            @JacksonXmlProperty(localName = "offers")
            OfferWrapper offers
    ) {
    }

    @Builder
    public record CategoryWrapper(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "category")
            List<Category> category
    ) {
    }

    @Builder
    public record OfferWrapper(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "offer")
            List<Offer> offer
    ) {
    }

    @Builder
    public record Category(
            @JacksonXmlProperty(isAttribute = true)
            String id,
            @JacksonXmlProperty(isAttribute = true)
            String parentId,
            @JacksonXmlText
            String title
    ) {
    }

    @Builder
    @JsonPropertyOrder({"id", "name", "vendor", "vendorCode", "url", "price",
            "currencyId", "categoryId", "picture", "description",
            "salesNotes", "manufacturerWarranty", "barcode",
            "param", "weight", "dimensions", "condition"})
    public record Offer(
            @JacksonXmlProperty(isAttribute = true)
            String id,

            String name,
            String vendor,
            String vendorCode,
            String url,
            int price,

            String currencyId,
            int categoryId,
            String picture,

            @JacksonXmlCData
            String description,

            @JacksonXmlProperty(localName = "sales_notes")
            String salesNotes,

            @JacksonXmlProperty(localName = "manufacturer_warranty")
            boolean manufacturerWarranty,

            String barcode,

            @JacksonXmlProperty(localName = "param")
            Param param,

            double weight,
            String dimensions,
            Condition condition
    ) {
    }

    @Builder
    public record Param(
            @JacksonXmlProperty(isAttribute = true)
            String name,
            @JacksonXmlText
            String value
    ) {
    }

    @Builder
    public record Condition(
            @JacksonXmlProperty(isAttribute = true)
            String type,
            String quality,

            String reason
    ) {
    }
}

