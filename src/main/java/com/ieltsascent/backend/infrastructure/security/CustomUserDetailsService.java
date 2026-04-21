package com.ieltsascent.backend.infrastructure.security;

import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username){
        String normalizedEmail = username.trim().toLowerCase(Locale.ROOT);
        return userRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}



