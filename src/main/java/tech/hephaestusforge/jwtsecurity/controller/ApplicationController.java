package tech.hephaestusforge.jwtsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hephaestusforge.jwtsecurity.model.jwt.AuthenticateRequestModel;
import tech.hephaestusforge.jwtsecurity.model.jwt.AuthenticationResponseModel;
import tech.hephaestusforge.jwtsecurity.model.jwt.RegisterRequestModel;
import tech.hephaestusforge.jwtsecurity.service.ApplicationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseModel> register(@RequestBody RegisterRequestModel request) {
        return ResponseEntity.ok(this.applicationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticateRequestModel request) {
        return ResponseEntity.ok(this.applicationService.authenticate(request));
    }
}
