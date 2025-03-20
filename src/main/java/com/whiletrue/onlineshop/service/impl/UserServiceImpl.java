package com.whiletrue.onlineshop.service.impl;

import com.whiletrue.onlineshop.exception.UserNotFoundException;
import com.whiletrue.onlineshop.model.User;
import com.whiletrue.onlineshop.repository.UserRepository;
import com.whiletrue.onlineshop.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        logger.info("User created successfully with email: {}", createdUser.getEmail());
        return createdUser;
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        logger.info("Is user with email: {} - {}", email, getUser.isPresent());
        return getUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        logger.info("User with ID {} deleted successfully", id);
    }
}
