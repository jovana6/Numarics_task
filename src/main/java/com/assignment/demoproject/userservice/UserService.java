package com.assignment.demoproject.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User getUserByUsername(String user) throws UsernameNotFoundException {
        return userRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException("com.assignment.demoproject.userservice.User not found"));
    }
}
