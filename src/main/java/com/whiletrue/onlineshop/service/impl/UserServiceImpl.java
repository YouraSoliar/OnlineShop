package com.whiletrue.onlineshop.service.impl;

import com.whiletrue.onlineshop.model.User;
import com.whiletrue.onlineshop.repository.UserRepository;
import com.whiletrue.onlineshop.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        User createdUser = userRepository.save(user);
        logger.info("User created successfully with email: {}", createdUser.getEmail());
        return createdUser;
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
