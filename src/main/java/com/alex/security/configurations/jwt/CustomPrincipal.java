package com.alex.security.configurations.jwt;

import com.alex.security.domain.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.security.Principal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {
    private String name; // email
    private Long userId;

    @Override
    public String getName() {
        return name;
    }

    public CustomPrincipal(User user) {
        this.userId = user.getId();
        this.name = user.getEmail();

    }
}
