package com.ehr.config.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ehr.entities.UserEntity;
import com.ehr.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byEmail = userRepository.findByEmail(username);

        if (byEmail == null) 
            throw new UsernameNotFoundException("Invalid credentials");

        return new MyUserDetails(byEmail);
    }
    
}
