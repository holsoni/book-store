package com.example.bookstore.service.impl;

import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.RoleName;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.RoleRepository;
import com.example.bookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "User " + requestDto.getEmail() + " already registered!");
        }
        Role role = roleRepository.getByName(RoleName.ROLE_USER);
        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(role));
        user.setPassword(new BCryptPasswordEncoder().encode(requestDto.getPassword()));
        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(new Role(RoleName.ROLE_USER)));
        return userMapper.toDto(userRepository.save(user));
    }
}
