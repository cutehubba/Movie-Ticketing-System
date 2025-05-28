package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.User;
import com.movie.ticketing.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated; // 导入 Validated
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Validated @RequestBody User user) { // 添加 Validated
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Received user data: " + user);
            User savedUser = userService.saveUser(user);
            response.put("code", 200);
            response.put("message", "注册成功");
            response.put("data", savedUser);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 400);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> data = userService.loginUser(credentials.get("username"), credentials.get("password"),credentials.get("role"));
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", data); // token + user
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 401);
            response.put("message", "登录失败：" + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            response.put("code", 200);
            response.put("message", "查询所有用户成功");
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                response.put("code", 200);
                response.put("message", "查询用户成功");
                response.put("data", user);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "用户未找到");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询用户出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @Validated @RequestBody User userDetails) { // 添加 Validated
        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            if (updatedUser != null) {
                response.put("code", 200);
                response.put("message", "用户更新成功");
                response.put("data", updatedUser);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "要更新的用户不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteUser(id);
            response.put("code", 200);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 404);
            response.put("message", "删除失败，用户不存在");
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}