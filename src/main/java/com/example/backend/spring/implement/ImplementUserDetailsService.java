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

    /**
     * Save the registered user, created the user folder accordingly
     * Return the newly created user
     *
     * @param user object of the registered user, consists of the user credentials
     */
    public User save(User user) {
        User newUser = userRepository.save(user);

        File file = new File("src/main/resources/users/" + newUser.getId());

        if (!file.exists()) {
            file.mkdir();
        }

        return newUser;
    }

    /**
     * Return true if verification token is verified
     * Return false if no user exists or user is already verified
     *
     * @param token verification token for user verification
     */
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

    /**
     * Return User object from database based on given username
     *
     * @param username string of username
     */
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

}