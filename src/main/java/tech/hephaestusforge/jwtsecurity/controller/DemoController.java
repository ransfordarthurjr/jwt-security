package tech.hephaestusforge.jwtsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hephaestusforge.jwtsecurity.service.ApplicationService;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {
    private final ApplicationService applicationService;

    @GetMapping()
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Hello from the other side!!!");
    }
}
