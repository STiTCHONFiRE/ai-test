package ru.stitchonfire.aitest.dto.xml;

public record SetDataDto(
        YmlRealStateCatalogDto.Set sellSet,
        YmlRealStateCatalogDto.Set dailySet,
        YmlRealStateCatalogDto.Set allSet
) {

}
