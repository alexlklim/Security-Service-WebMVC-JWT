package com.alex.security.configurations.jwt;

import com.alex.security.domain.User;
import com.alex.security.email.EmailService;
import com.alex.security.email.EmailStructure;
import com.alex.security.email.EmailType;
import com.alex.security.exceptions.errors.UserFailedAuthentication;
import com.alex.security.domain.Token;
import com.alex.security.dto.AuthDto;
import com.alex.security.dto.LoginDto;
import com.alex.security.dto.TokenDto;
import com.alex.security.utils.UtilsSecurity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final String TAG = "AUTHENTICATION_SERVICE - ";

    private final UserAuthService userAuthService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailService emailService;

    @SneakyThrows
    public AuthDto authenticate(LoginDto request) {
        log.info(TAG + "Try to authenticate user with email: {}", request.getEmail());
        User user = userAuthService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserFailedAuthentication("Authentication failed"));
        if (!user.isEnabled())
            throw new UserFailedAuthentication("Authentication failed");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return getAuthDto(user);
    }

    private AuthDto getAuthDto(User user) {
        log.info(TAG + "Get authentication dto for user with email: {}", user.getEmail());
        Token refreshToken = tokenService.createRefreshToken(user);
        AuthDto authDto = new AuthDto();
        authDto.setFirstName(user.getFirstname());
        authDto.setLastName(user.getLastname());
        authDto.setExpiresAt(tokenService.getTokenById(refreshToken.getId()).getExpired());
        authDto.setAccessToken(jwtService.generateToken(new CustomPrincipal(user)));
        authDto.setRefreshToken(refreshToken.getToken());
        authDto.setRole(Collections.singletonList(user.getRoles().name()));
        return authDto;
    }

    @SneakyThrows
    public AuthDto refreshToken(TokenDto request) {
        log.info(TAG + "Refresh access and refresh tokens for user: {}", request.getEmail());
        User user = userAuthService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserFailedAuthentication("Authentication failed"));
        if (!tokenService.checkIfTokenValid(request.getRefreshToken(), user))
            throw new UserFailedAuthentication("Authentication failed");
        tokenService.deleteTokenByUser(user);
        return getAuthDto(user);
    }

    @SneakyThrows
    public void forgotPasswordAction(String email) {
        log.info(TAG + "Forgot password action: {}", email);
        User user = userAuthService.getUserByEmail(email)
                .orElseThrow(() -> new UserFailedAuthentication("Authentication failed"));
        tokenService.deleteTokenByUser(user);
        Token token = tokenService.createRefreshToken(user);

        emailService.createMail(
                EmailStructure.builder()
                        .email(user.getEmail())
                        .emailType(EmailType.FORGOT_PASSWORD)
                        .build(),
                emailService.createBody(
                        EmailType.FORGOT_PASSWORD,
                        UtilsSecurity.ENDPOINT_RECOVERY + token.getToken().toString())
        );

    }

    @SneakyThrows
    public void recoveryPassword(String token, String password) {
        log.info(TAG + "Recovery password for user with token: {}", token);
        Token tokenFromDB = tokenService.getTokenByToken(UUID.fromString(token))
                .orElseThrow(() -> new UserFailedAuthentication("Authentication failed"));
        User user = userAuthService.getById(tokenFromDB.getUser().getId());
        tokenService.deleteTokenByUser(user);
        userAuthService.changePasswordForgot(user.getEmail(), password);

        emailService.createMail(
                EmailStructure.builder()
                        .email(user.getEmail())
                        .emailType(EmailType.PASSWORD_WAS_CHANGED)
                        .build(),
                emailService.createBody(
                        EmailType.PASSWORD_WAS_CHANGED));
    }

    @Transactional
    public void logout(CustomPrincipal principal) {
        log.info(TAG + "Logout user with email {}", principal.getName());
        User user = userAuthService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserFailedAuthentication("Authentication failed"));
        tokenService.deleteTokenByUser(user);
        SecurityContextHolder.clearContext();
    }
}
