package ru.stitchonfire.aitest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)   // на всякий случай
public record TestResponseDto(
        String name  // добавили
) {}
