package com.example.likelion14th_springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        // Postman에서 JSON 형태로 보일 수 있도록 Map 반환
        return Map.of("status", "UP");
    }
}