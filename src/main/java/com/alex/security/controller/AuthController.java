package com.alex.security.controller;


import com.alex.security.dto.*;
import com.alex.security.configurations.jwt.AuthenticationService;
import com.alex.security.configurations.jwt.UserAuthService;
import com.alex.security.services.UserService;
import com.alex.security.utils.SecHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${security.auth.base-url}/auth")
@RequiredArgsConstructor
@Tag(name = "Security Controller", description = "Security API")
public class AuthController {

    private final String TAG = "SECURITY_CONTROLLER - ";

    private final UserAuthService userAuthService;
    private final AuthenticationService authenticationService;
    private final UserService userService;



    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user and get Auth DTO")
    @PostMapping("/login")
    public AuthDto authenticate(
            @RequestBody LoginDto loginDto) {
        log.info(TAG + "authenticate");
        return authenticationService.authenticate(loginDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "logout user, inactive refresh token")
    @GetMapping("/logout")
    public void logout() {
        log.info(TAG + "logout");
        authenticationService.logout(SecHolder.getPrincipal());
    }



    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "refresh token and get new Auth DTO")
    @PostMapping("/refresh")
    public AuthDto refreshToken(
            @RequestBody TokenDto tokenDto) {
        log.info(TAG + "refreshToken");
        return authenticationService.refreshToken(tokenDto);
    }




    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change password for authenticated users")
    @PostMapping("/pw/change")
    public void changePasswordForAuthUsers(
            @RequestBody PasswordDto passwordDto) {
        log.info(TAG + "changePasswordForAuthUsers");
        userAuthService.changePassword(
                passwordDto,
                SecHolder.getPrincipal());

    }





    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "send link to email to recovery password")
    @PostMapping("/pw/recovery")
    public void sendLinkToPasswordRecovery(
            @RequestBody DtoName emailDto) {
        log.info(TAG + "sendLinkToPasswordRecovery");
        authenticationService.forgotPasswordAction(emailDto.getEmail());

    }
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change password using token and link from email")
    @PostMapping("/pw/recovery/{token}")
    public void recoveryPassword(
            @PathVariable("token") String token,
            @RequestBody PasswordRecoveryDTO passwordDto) {
        log.info(TAG + "recoveryPassword");
        authenticationService.recoveryPassword(token, passwordDto.getPassword());
    }





    @Operation(summary = "Register new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserDto register(
            @RequestBody RegisterDto registerDto) {
        log.info(TAG + "Register new user");
        userAuthService.register(registerDto);
        return null;
    }



    @Operation(summary = "Activate user account by link from email")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/activate/{token}")
    public UserDto activate(
            @PathVariable("token") String token) {
        log.info(TAG + "Register new user");
        userAuthService.activate(
                token);
        return null;
    }




}