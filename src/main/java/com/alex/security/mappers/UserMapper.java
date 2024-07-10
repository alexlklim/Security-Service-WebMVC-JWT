package com.alex.security.mappers;

import com.alex.security.domain.User;
import com.alex.security.dto.UserDto;
import com.alex.security.domain.Role;
import com.alex.security.dto.RegisterDto;
import com.alex.security.utils.DateService;
import lombok.Data;

@Data
public class UserMapper {

    public static User toUser(RegisterDto dto) {
        User user = new User();
        user.setFirstname(dto.getFirstName());
        user.setLastname(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRoles(Role.fromString(dto.getRole()));
        user.setActive(false);
        user.setLastActivity(DateService.getDateNow());
        return user;
    }


    public static UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstname());
        dto.setLastName(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setRole(entity.getRoles().name());
        dto.setLastActivity(DateService.getDateNow());
        dto.setLastActivity(entity.getLastActivity());
        return dto;
    }


}
