package com.example.uberbackend.service;

import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username doesn't exist"));
    }

    public void processOAuthPostLogin(String username) throws UsernameNotFoundException {
        com.example.uberbackend.model.User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username doesn't exist"));
    }
}
