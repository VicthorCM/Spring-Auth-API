package com.github.victhorcm.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.github.victhorcm.auth.model.RefreshToken;
import com.github.victhorcm.auth.model.User;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long >{

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user); 
}
