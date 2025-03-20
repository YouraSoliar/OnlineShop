package com.whiletrue.onlineshop.service;

import com.whiletrue.onlineshop.model.User;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserByEmail(String email);
    void deleteUser(Long id);
}
