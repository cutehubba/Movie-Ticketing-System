package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.User;
import com.movie.ticketing.userservice.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepositoryImpl userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepositoryImpl userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码
        return userRepository.saveUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setNickname(userDetails.getNickname());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword())); // 加密密码
        user.setRole(userDetails.getRole()); // ✨ 新增：更新角色
        return userRepository.saveUser(user);
    }


    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public Map<String, Object> loginUser(String username, String password, String role) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) { // 验证密码

            if (!user.getRole().equals(role)) {
                throw new RuntimeException("角色验证失败");
            }
            Map<String, Object> response = new HashMap<>();
            String token = jwtService.generateToken(user.getId(), user.getUsername());
            response.put("token", token);
            response.put("user", user);
            response.put("role", user.getRole());
            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}