package ru.stitchonfire.aitest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stitchonfire.aitest.dto.TestRequestDto;
import ru.stitchonfire.aitest.dto.TestResponseDto;
import ru.stitchonfire.aitest.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    TestService testService;

    @PostMapping("send")
    public ResponseEntity<TestResponseDto> test(@RequestBody TestRequestDto request) {
        return ResponseEntity.ok(testService.getTestResponse(request));
    }

}
