package com.alex.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "User DTO")
public class UserDto {
    @Schema(description = "User id", example = "10")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;


    @Schema(description = "Email", example = "alex@gmail.com")
    String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Password", example = "1122")
    String password;


    @Schema(description = "First name", example = "Alex")
    String firstName;

    @Schema(description = "Last name", example = "Klim")
    String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Is active", example = "true")
    boolean isActive;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Last activity", example = "2024-03-08 14:30")
    LocalDateTime lastActivity;

    @Schema(description = "Role", example = "ADMIN")
    String role;


    @Schema(description = "Start license date", example = "2024-03-08")
    LocalDateTime activateLicenseDate;

    @Schema(description = "End License date", example = "2025-03-08")
    LocalDateTime endLicenseDate;


    @Schema(description = "License_id", example = "2")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long licenseId;




}