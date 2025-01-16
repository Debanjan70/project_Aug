package com.app.service;

import com.app.entity.User;
import com.app.exception.EmailAlreadyExistsException;
import com.app.exception.UsernameAlreadyExistsException;
import com.app.payload.UserDto;
import com.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    public AuthService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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
        User save = userRepository.save(user);
        UserDto userDto = ConvertEntityToDto(save);
        return userDto;
    }

    UserDto ConvertEntityToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

}
