package com.app.controller;

import com.app.entity.User;
import com.app.payload.LoginDto;
import com.app.payload.UserDto;
import com.app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(
            @RequestBody User user
    ){
        UserDto userDto = authService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestBody LoginDto loginDto
            ){
        boolean status = authService.verifyLogin(loginDto);
        if(status){
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username/Password", HttpStatus.UNAUTHORIZED);
    }
}
