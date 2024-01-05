package com.OAuth2.AuthorizationServer.security.services;

import com.OAuth2.AuthorizationServer.repositories.UserRepository;
import com.OAuth2.AuthorizationServer.security.models.CustomUserDetails;
import com.OAuth2.AuthorizationServer.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return new CustomUserDetails(user.get());
    }
}
