package com.ISL.ISL.service;

import com.ISL.ISL.config.UserDetailsImpl;
import com.ISL.ISL.model.User;
import com.ISL.ISL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + identifier));

        System.out.println("🔹 Loaded User: " + user.getEmail() + " | Password: " + user.getPassword()); // Debugging

        return new UserDetailsImpl(user);
    }

}
