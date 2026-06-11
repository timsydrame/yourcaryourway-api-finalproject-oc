package com.yourcaryourway.api.service;

import com.yourcaryourway.api.dto.ChangePasswordRequest;
import com.yourcaryourway.api.repository.UserRepository;
import com.yourcaryourway.api.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void changePassword(
            String email,
            ChangePasswordRequest request) {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mot de passe actuel incorrect");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmNewPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Les nouveaux mots de passe ne correspondent pas");
        }

        user.setPassword(passwordEncoder
                .encode(request.getNewPassword()));
        userRepository.save(user);
    }
}