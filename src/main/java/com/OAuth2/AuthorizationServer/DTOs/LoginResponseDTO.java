package com.OAuth2.AuthorizationServer.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Long userId;
    private String email;
    private String token;
}
