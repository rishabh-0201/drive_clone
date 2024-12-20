package com.org.googledrive.Controllers;

import com.org.googledrive.Service.UserService;
import com.org.googledrive.models.User;
import com.org.googledrive.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;
    // Register a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){

        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){

        boolean isValid = userService.validateUser(user.getEmail(),user.getPassword());

        if(isValid){
            String token = jwtUtil.generateToken(user.getEmail());

            // return the token as a JSON response
            return ResponseEntity.ok(Map.of("token",token));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
