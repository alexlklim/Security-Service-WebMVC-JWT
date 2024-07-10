package com.alex.security.controller;


import com.alex.security.dto.UserDto;
import com.alex.security.configurations.jwt.UserAuthService;
import com.alex.security.services.UserService;
import com.alex.security.utils.SecHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${security.auth.base-url}/auth")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final String TAG = "ADMIN_CONTROLLER - ";

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get user info")
    @GetMapping
    public UserDto getUser() {
        log.info(TAG + "getUser");
        return userService.getUserDTOById(SecHolder.getUserId());
    }

    @Operation(summary = "Update user")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public void updateUser(
            @RequestBody UserDto userDto) {
        log.info(TAG + "updateUser");
        userService.updateUser(SecHolder.getUserId(), userDto);
    }




    @Operation(summary = "Get info about all user by id (only for admin)")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info(TAG + "getAllUsers");
        return userService.getAllUsers();

    }
}
