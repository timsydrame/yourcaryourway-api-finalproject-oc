package com.yourcaryourway.api.controller;

import com.yourcaryourway.api.dto.ChangePasswordRequest;
import com.yourcaryourway.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "Bearer Authentication")

public class UserController {
    private final UserService userService;

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request) {

        String email = jwt.getSubject();
        userService.changePassword(email, request);

        return ResponseEntity.ok(
                Map.of("message",
                        "Mot de passe modifié avec succès"));
    }}