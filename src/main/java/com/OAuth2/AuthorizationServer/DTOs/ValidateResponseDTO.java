package com.OAuth2.AuthorizationServer.DTOs;

import com.OAuth2.AuthorizationServer.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidateResponseDTO {
    private Long userId;
    private String email;
    private List<Role> roles;
}
