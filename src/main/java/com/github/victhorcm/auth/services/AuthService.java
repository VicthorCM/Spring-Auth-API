package com.github.victhorcm.auth.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.victhorcm.auth.dto.RefreshTokenResponseDTO;
import com.github.victhorcm.auth.model.RefreshToken;
import com.github.victhorcm.auth.model.User;
import com.github.victhorcm.auth.repository.RefreshTokenRepository;
import com.github.victhorcm.auth.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository ;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    public AuthService (UserRepository repository, BCryptPasswordEncoder encoder, TokenService tokenService, RefreshTokenService refreshTokenService,RefreshTokenRepository refreshTokenRepository){
        this.userRepository = repository;
        this.passwordEncoder = encoder;
        this.tokenService = tokenService;
        this.refreshTokenService= refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public User signUp(String email, String password){

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required!");
        }

        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("This email is already registered!");
        }

        

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);

        return  userRepository.save(user);
    }

    public RefreshTokenResponseDTO login(String email, String password){

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required!");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

       

        if(!passwordEncoder.matches(password,user.getPasswordHash())){
            throw new RuntimeException("This password is invalid!");
            
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        refreshTokenRepository.save(refreshToken);

        String accesstoken = tokenService.generateToken(user);


        
        return new RefreshTokenResponseDTO(accesstoken,refreshToken.getToken());
    }

    
}

