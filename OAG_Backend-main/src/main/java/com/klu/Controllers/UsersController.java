package com.klu.Controllers;

import com.klu.Models.Users;
import com.klu.Services.UserService;
import com.klu.DTO.LoginRequest;
import com.klu.DTO.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    // Get a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable int id) {
        Optional<Users> user = userService.getUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Add a new user
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        if (userDto == null) return ResponseEntity.badRequest().build();

        // Check if email exists
        boolean exists = userService.getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(userDto.getEmail()));
        if (exists) return ResponseEntity.status(409).build(); // Conflict

        Users user = new Users();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword()); // Will be hashed in service

        Users createdUser = userService.addUser(user);

        UserDto responseDto = new UserDto();
        responseDto.setId(createdUser.getId());
        responseDto.setName(createdUser.getName());
        responseDto.setEmail(createdUser.getEmail());
        responseDto.setPhone(createdUser.getPhone());

        return ResponseEntity.ok(responseDto);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable int id, @RequestBody Users user) {
        if (id != user.getId()) return ResponseEntity.badRequest().build();
        userService.updateUser(user);
        return ResponseEntity.noContent().build();
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginDto) {
        if (loginDto == null) return ResponseEntity.badRequest().body("Invalid client request");

        Users user = userService.authenticate(loginDto.getEmail(), loginDto.getPassword());
        if (user == null) return ResponseEntity.status(401).body("Invalid email or password");

        // Build response
        return ResponseEntity.ok(new Object() {
            public final String message = "Login successful";
            public final Object userDetails = new Object() {
                public final int id = user.getId();
                public final String name = user.getName();
                public final String email = user.getEmail();
                public final String phone = user.getPhone();
            };
        });
    }
}
