package ru.stitchonfire.aitest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stitchonfire.aitest.dto.AnnouncementRequestDto;
import ru.stitchonfire.aitest.dto.AnnouncementDataDto;
import ru.stitchonfire.aitest.service.YmlCatalogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    YmlCatalogService testService;

    @PostMapping("send")
    public ResponseEntity<AnnouncementDataDto> test(@RequestBody AnnouncementRequestDto request) {
        return ResponseEntity.ok(testService.getAiResponse(request));
    }

}
