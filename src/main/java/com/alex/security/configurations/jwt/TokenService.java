package com.alex.security.configurations.jwt;

import com.alex.security.domain.User;
import com.alex.security.exceptions.errors.UserFailedAuthentication;
import com.alex.security.repo.TokenRepo;
import com.alex.security.domain.Token;
import com.alex.security.utils.DateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Transactional
@Slf4j @RequiredArgsConstructor @Service
public class TokenService {
    private final TokenRepo tokenRepo;

    private final String TAG = "TOKEN SERVICE - ";


    public void deleteTokenByUser(User user) {
        log.info(TAG + "Delete refresh token for user with email: {}", user.getEmail());
        tokenRepo.deleteAllByUser(user);
    }

    public Token getTokenById(Long tokenID){
        log.info(TAG + "Get token by id {}", tokenID);
        return tokenRepo.findById(tokenID).orElse(null);
    }

    public Token createRefreshToken(User user){
        log.info(TAG + "Create refresh token for user with email: {}", user.getEmail());
        tokenRepo.deleteAllByUser(user);
        Token token = new Token();
        token.setUser(user);
        token.setToken(UUID.randomUUID());
        token.setCreated(DateService.getDateNow());
        token.setExpired(DateService.addOneDayToDate(DateService.getDateNow()));
        return tokenRepo.save(token);
    }


    @SneakyThrows
    public boolean checkIfTokenValid(UUID refreshToken, User user) {
        log.info(TAG + "Check if token belong to user: {} and not expired", user.getEmail());
        Token token = tokenRepo.findByUserAndToken(user, refreshToken)
                .orElseThrow(() -> new UserFailedAuthentication("User authentication failed"));
        return token.getExpired() != null && token.getExpired().isBefore(LocalDateTime.now());
    }


    public Optional<Token> getTokenByToken(UUID refreshToken) {
        log.info(TAG + "Get token by token {}", refreshToken);
        return tokenRepo.findByToken(refreshToken);
    }
}