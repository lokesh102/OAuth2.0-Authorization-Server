package com.OAuth2.AuthorizationServer.DTOs;
 import com.OAuth2.AuthorizationServer.models.User;
 import lombok.Getter;
 import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;

    public static UserDTO from(User newUser) {
        UserDTO signUpResponseDTO = new UserDTO();
        signUpResponseDTO.setEmail(newUser.getEmail());
        return signUpResponseDTO;
    }
}
