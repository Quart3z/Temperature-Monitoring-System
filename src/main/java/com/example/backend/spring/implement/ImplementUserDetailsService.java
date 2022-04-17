package com.example.backend.spring.implement;

import com.example.backend.spring.entity.User;
import com.example.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImplementUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);

        if (user == null || user.getStatus() == 0) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    // New user creation
    public User save(User user) {
        User newUser = userRepository.save(user);

        File file = new File("src/main/resources/users/" + newUser.getId());

        if (!file.exists()) {
            file.mkdir();
        }

        return newUser;

    }

    // New user verification
    public boolean verification(String token) {
        User user = userRepository.findUserByToken(token);

        if (user == null || user.getStatus() != 0) {
            return false;
        } else {
            user.setStatus(1);
            userRepository.save(user);

            return true;
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

}