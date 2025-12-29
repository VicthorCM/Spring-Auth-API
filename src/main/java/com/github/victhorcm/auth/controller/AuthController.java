package com.github.victhorcm.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.victhorcm.auth.dto.LoginRequestDTO;
import com.github.victhorcm.auth.dto.LoginResponseDTO;
import com.github.victhorcm.auth.dto.RefreshTokenRequestDTO;
import com.github.victhorcm.auth.dto.RefreshTokenResponseDTO;
import com.github.victhorcm.auth.dto.RegisterRequestDTO;
import com.github.victhorcm.auth.dto.RegisterResponseDTO;
import com.github.victhorcm.auth.model.RefreshToken;
import com.github.victhorcm.auth.model.User;
import com.github.victhorcm.auth.services.AuthService;
import com.github.victhorcm.auth.services.RefreshTokenService;
import com.github.victhorcm.auth.services.TokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDTO> signUp( @RequestBody RegisterRequestDTO body){

        User newUser = authService.signUp(body.email(), body.password());

        RegisterResponseDTO response = new RegisterResponseDTO(newUser.getEmail());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login( @RequestBody LoginRequestDTO body){

        RefreshTokenResponseDTO tokensResponse = authService.login(body.email(), body.password());

    

        LoginResponseDTO response = new LoginResponseDTO(tokensResponse.token(),tokensResponse.refreshToken());


        return ResponseEntity.status(200).body(response);

    }

    @Transactional
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String requestToken = request.refreshToken();
        if (requestToken == null || requestToken.isBlank()) {
            throw new IllegalArgumentException("Refresh Token is required");
        }


        String requestRefreshToken = request.refreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).orElseThrow(() -> new RuntimeException("Refresh token Not Found"));

        refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        
        User user = refreshToken.getUser();

        String newAccessToken= tokenService.generateToken(user);

        refreshTokenService.delete(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new RefreshTokenResponseDTO(newAccessToken,newRefreshToken.getToken()));
        }   
        
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user){
        
        refreshTokenService.deleteByUserId(user.getId());
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok("Log out successful!");


    }
    }


