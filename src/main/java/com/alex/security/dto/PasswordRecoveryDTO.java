package com.alex.security.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryDTO {

    private String password;

    public PasswordRecoveryDTO() {
    }

    public PasswordRecoveryDTO(String password) {
        this.password = password;
    }
}
