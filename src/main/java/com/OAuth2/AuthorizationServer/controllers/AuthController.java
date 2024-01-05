package com.OAuth2.AuthorizationServer.controllers;


import com.OAuth2.AuthorizationServer.DTOs.*;
import com.OAuth2.AuthorizationServer.exceptions.EmailAlreadyExistsException;
import com.OAuth2.AuthorizationServer.exceptions.UserNotFoundException;
import com.OAuth2.AuthorizationServer.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) throws EmailAlreadyExistsException {
        Optional<UserDTO> signUpResponseDTO =  authService.signUp(signUpRequestDTO.getEmail(),signUpRequestDTO.getPassword());
        if(signUpResponseDTO.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(signUpResponseDTO.get(), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws UserNotFoundException {
        Optional<LoginResponseDTO> loginResponseDTO = authService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        if(!loginResponseDTO.isEmpty()){
            LoginResponseDTO loginResponseDTO1 = loginResponseDTO.get();
            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add("AUTH-HEADER",loginResponseDTO1.getToken());
            headers.add("USER-ID",loginResponseDTO1.getUserId().toString());
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(loginResponseDTO1.getEmail());
            return new ResponseEntity<>(userDTO,headers,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/logout")
    public ResponseEntity<LogOutResponseDTO> logout(@RequestHeader(value = "AUTH-HEADER") String token, @RequestHeader(value = "USER_ID") Long userId){
       LogOutResponseDTO logOutResponseDTO =  authService.logout(token,userId);
       return new ResponseEntity<>(logOutResponseDTO,HttpStatus.OK);
    }
    @PostMapping("/validate")
    public ResponseEntity<ValidateResponseDTO> validate(@RequestBody ValidateRequestDTO validateRequestDTO) throws UserNotFoundException {
        if(validateRequestDTO == null || validateRequestDTO.getToken() == null || validateRequestDTO.getUserId() == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        ValidateResponseDTO validateResponseDTO = authService.validate(validateRequestDTO.getToken(),validateRequestDTO.getUserId());
        return new ResponseEntity<>(validateResponseDTO,HttpStatus.OK);

    }
}
