package com.app.controller;

import com.app.entity.User;
import com.app.payload.JWTTokenDto;
import com.app.payload.LoginDto;
import com.app.payload.UserDto;
import com.app.service.AuthService;
import com.app.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")     //http://localhost:8080/api/v1/auth/
public class AuthController {
    private AuthService authService;
    private OtpService otpService;
    public AuthController(AuthService authService , OtpService otpService) {
        this.authService = authService;
        this.otpService =  otpService;
    }
    @PostMapping("/user-signup")
    public ResponseEntity<UserDto> createUser(
            @RequestBody User user
    ){
        UserDto userDto = authService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/content-manager-signup")
    public ResponseEntity<UserDto> createContentManagerAccount(
            @RequestBody User user
    ){
        UserDto userDto = authService.createContentManagerAccount(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/blog-manager-signup")
    public ResponseEntity<UserDto> createBlogManagerAccount(
            @RequestBody User user
    ){
        UserDto userDto = authService.createBlogManagerAccount(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginDto loginDto
            ){
        String jwtToken = authService.verifyLogin(loginDto);
        if(jwtToken != null){
            JWTTokenDto tokenDto = new JWTTokenDto();
            tokenDto.setToken(jwtToken);
            tokenDto.setTokenType("JWT");
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login-otp")
    public ResponseEntity<?> genrateOtp(
            @RequestParam String mobile
    ){
        String otp = otpService.genarateOtp(mobile);
        return new ResponseEntity<>(mobile + " Otp is: " + otp, HttpStatus.OK);
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String mobile ,
            @RequestParam String otp
            ){
        boolean status = otpService.validateOtp(mobile, otp);

        if(status){
            //genarate jwt token
            String jwtToken = authService.genarateToken(mobile);
            if(jwtToken != null){
                JWTTokenDto tokenDto = new JWTTokenDto();
                tokenDto.setToken(jwtToken);
                tokenDto.setTokenType("JWT");
                return new ResponseEntity<>(tokenDto, HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>("Invalid Oto" , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
