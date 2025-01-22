package com.app.service;

import com.app.entity.User;
import com.app.exception.EmailAlreadyExistsException;
import com.app.exception.UsernameAlreadyExistsException;
import com.app.payload.LoginDto;
import com.app.payload.UserDto;
import com.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private JWTService jwtService;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper, JWTService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;

    }

    public UserDto createUser(User user){
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        Optional<User> opEmail = userRepository.findByEmailId(user.getEmailId());
        if(opEmail.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        //Here we are doing password encription is a 1st Way to encrpt
//        String encodePassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodePassword);

        //Here we are doing password encription is a 2nd Way to encrpt:-

        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);

        User save = userRepository.save(user);
        UserDto userDto = ConvertEntityToDto(save);
        return userDto;
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<User> opUser = userRepository.findByUsername(loginDto.getUsername());
        if(opUser.isPresent()){
            User user = opUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
              return   jwtService.generateToken(user.getUsername());
            }
        }
        return null;
    }

    UserDto ConvertEntityToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

}
