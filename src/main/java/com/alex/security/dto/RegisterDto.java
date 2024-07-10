package com.alex.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Register DTO")
public class RegisterDto {

    @Schema(description = "First name", example = "Alex")
    String firstName;

    @Schema(description = "Last name", example = "Klim")
    String lastName;

    @Schema(description = "Email", example = "alex@gmail.com")
    String email;

    @Schema(description = "Password", example = "1122")
    String password;
}


