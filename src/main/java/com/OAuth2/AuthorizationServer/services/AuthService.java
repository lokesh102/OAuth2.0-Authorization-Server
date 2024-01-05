package com.OAuth2.AuthorizationServer.services;


import com.OAuth2.AuthorizationServer.DTOs.LogOutResponseDTO;
import com.OAuth2.AuthorizationServer.DTOs.LoginResponseDTO;
import com.OAuth2.AuthorizationServer.DTOs.UserDTO;
import com.OAuth2.AuthorizationServer.DTOs.ValidateResponseDTO;
import com.OAuth2.AuthorizationServer.exceptions.EmailAlreadyExistsException;
import com.OAuth2.AuthorizationServer.exceptions.UserNotFoundException;
import com.OAuth2.AuthorizationServer.models.Role;
import com.OAuth2.AuthorizationServer.models.Session;
import com.OAuth2.AuthorizationServer.models.SessionStatus;
import com.OAuth2.AuthorizationServer.models.User;
import com.OAuth2.AuthorizationServer.repositories.RoleRepository;
import com.OAuth2.AuthorizationServer.repositories.SessionRepository;
import com.OAuth2.AuthorizationServer.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SessionRepository sessionRepository;
    private RoleRepository roleRepository;
    public AuthService(UserRepository userRepository,SessionRepository sessionRepository,PasswordEncoder passwordEncoder,RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    public Optional<UserDTO> signUp(String email, String password) throws EmailAlreadyExistsException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()){
            throw new EmailAlreadyExistsException(email+" already exists");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        Role currRole = roleRepository.findByName("USER");
        roles.add(currRole);
        newUser.setRoles(roles);
        userRepository.save(newUser);

        return Optional.of(UserDTO.from(newUser));
    }
    public Optional<LoginResponseDTO> login(String email, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(email +" no user with this email");
        }
        User user = userOptional.get();
        if(user.getEmail().equals(email) && user.getPassword().equals(password)){
            //String token = RandomStringUtils.randomAscii(20);
            String token1 = RandomStringUtils.randomAlphanumeric(20);
            Session session = new Session();
            session.setUser(user);
            session.setToken(token1);
            session.setSessionStatus(SessionStatus.ACTIVE);
            sessionRepository.save(session);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUserId(user.getId());
            loginResponseDTO.setEmail(user.getEmail());
            loginResponseDTO.setToken(session.getToken());
            return Optional.of(loginResponseDTO);
        }
            throw new UserNotFoundException(email +" no user found");
        //return null;
    }
    public LogOutResponseDTO logout(String token, Long userId){
        LogOutResponseDTO logOutResponseDTO = new LogOutResponseDTO();
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            logOutResponseDTO.setStatus("user not available");
        }
        User user1 = user.get();
        Optional<Session> session = sessionRepository.findByTokenAndUser(token,user1);
        if(session.isPresent()){
            Session currentSession = session.get();
            currentSession.setSessionStatus(SessionStatus.EXPIRED);
            sessionRepository.save(currentSession);
            logOutResponseDTO.setStatus("user id "+userId+" logged out.");
            return logOutResponseDTO;
        }
        logOutResponseDTO.setStatus("user not found");
        return logOutResponseDTO;

    }
    public ValidateResponseDTO validate(String token, Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("user not found");
        }
        Optional<Session> session = sessionRepository.findByTokenAndUser(token,user.get());
        if(session.isPresent()){
            Session session1 = session.get();
            if(session1.getSessionStatus() == SessionStatus.EXPIRED){
                throw new UserNotFoundException("user not found");
            }
            Date tokenDate = session1.getExpiry();
            Date currDate = Date.from(Instant.now());
            if(tokenDate != null && tokenDate.before(currDate)){
                session1.setSessionStatus(SessionStatus.EXPIRED);
                sessionRepository.save(session1);
                throw new UserNotFoundException("user not found");
            }
            ValidateResponseDTO validateResponseDTO = new ValidateResponseDTO();
            validateResponseDTO.setRoles(user.get().getRoles());
            validateResponseDTO.setEmail(user.get().getEmail());
            validateResponseDTO.setUserId(userId);
            return validateResponseDTO;
        }
        throw new UserNotFoundException("user not found");
    }
}
