package com.alex.security.configurations.jwt;

import com.alex.security.domain.Role;
import com.alex.security.domain.User;
import com.alex.security.email.EmailService;
import com.alex.security.email.EmailStructure;
import com.alex.security.email.EmailType;
import com.alex.security.exceptions.errors.UserNotRegisterYet;
import com.alex.security.exceptions.errors.ObjectAlreadyExistException;
import com.alex.security.exceptions.errors.ResourceNotFoundException;
import com.alex.security.mappers.UserMapper;
import com.alex.security.repo.UserRepo;
import com.alex.security.domain.Token;
import com.alex.security.dto.PasswordDto;
import com.alex.security.dto.RegisterDto;
import com.alex.security.utils.UtilsSecurity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService {
    private final String TAG = "USER_AUTHENTICATION_SERVICE - ";

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final EmailService emailService;


    private final TokenService tokenService;


    @SneakyThrows
    public void register(RegisterDto dto) {
        log.info(TAG + "Register user with email: {}", dto.getEmail());
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new ObjectAlreadyExistException("User with email {} is already exists");
        }
        User user = UserMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Role.USER);
        User savedUser = userRepo.save(user);
        Token token = tokenService.createRefreshToken(savedUser);

        emailService.createMail(
                EmailStructure.builder()
                        .email(user.getEmail())
                        .emailType(EmailType.CONFIRM_EMAIL)
                        .build(),
                emailService.createBody(
                        EmailType.CONFIRM_EMAIL,
                        UtilsSecurity.ENDPOINT_ACTIVATE + token.getToken().toString())
        );

    }


    @SneakyThrows
    public void changePassword(PasswordDto dto, CustomPrincipal principal) {
        log.info(TAG + "Change password for user with email: {}", principal.getName());
        User user = userRepo.getUser(principal.getUserId());
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword()))
            throw new AuthenticationException("Password is wrong");
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);


        emailService.createMail(
                EmailStructure.builder()
                        .email(user.getEmail())
                        .emailType(EmailType.PASSWORD_WAS_CHANGED)
                        .build(),
                emailService.createBody(EmailType.PASSWORD_WAS_CHANGED)
        );


    }


    @SneakyThrows
    public void activate(String tokenString) {
        Token token = tokenService.getTokenByToken(UUID.fromString(tokenString))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Token not found")
                );
        User user =  token.getUser();
        user.setActive(true);
        tokenService.deleteTokenByUser(user);
        userRepo.save(user);
    }


    @SneakyThrows
    public void changePasswordForgot(String email, String password) {
        log.info(TAG + "Change password for user: {}", email);
        User user = userRepo.getUserByEmail(email).orElseThrow(
                () -> new UserNotRegisterYet("User with email " + email + " is not registered"));
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }


    public User getById(Long userId) {
        log.info(TAG + "Exists by user id: {}", userId);
        return userRepo.findById(userId).orElse(null);
    }

    @Transactional
    public void updateLastActivity(Long userId) {
        User user = userRepo.getUser(userId);
        user.setLastActivity(LocalDateTime.now());
        userRepo.save(user);
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }


}