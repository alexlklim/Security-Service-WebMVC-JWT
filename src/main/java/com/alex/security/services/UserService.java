package com.alex.security.services;

import com.alex.security.domain.User;
import com.alex.security.dto.UserDto;
import com.alex.security.exceptions.errors.ResourceNotFoundException;
import com.alex.security.mappers.UserMapper;
import com.alex.security.repo.UserRepo;
import com.alex.security.domain.Role;
import com.alex.security.configurations.jwt.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final String TAG = "USER_SERVICE - ";
    private final UserRepo userRepo;
    private final UserAuthService userAuthService;

    public UserDto getUserDTOById(Long id) {
        return UserMapper.toDto(userRepo.getUser(id));
    }

    public List<UserDto> getAllUsers() {
        log.info(TAG + "Get all users");
        return userRepo.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

    }

    @SneakyThrows
    public void updateUser(Long userId, UserDto dto) {
        log.error(TAG + "updateUserByAdmin");
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + userId + " not found"));
        user.setFirstname(dto.getFirstName());
        user.setLastname(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRoles(Role.fromString(dto.getRole()));
        user.setActive(dto.isActive());
        userAuthService.changePasswordForgot(dto.getPassword(), dto.getEmail());
        userRepo.save(user);
    }

}