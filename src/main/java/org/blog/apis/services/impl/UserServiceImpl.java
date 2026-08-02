package org.blog.apis.services.impl;

import org.blog.apis.entities.Role;
import org.blog.apis.entities.User;
import org.blog.apis.exceptions.ResourceNotFoundException;
import org.blog.apis.payloads.UserRequestDto;
import org.blog.apis.payloads.UserResponseDto;
import org.blog.apis.repositories.RoleRepo;
import org.blog.apis.repositories.UserRepositories;
import org.blog.apis.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserServices {
    private final UserRepositories repositories;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public UserServiceImpl(UserRepositories repositories, ModelMapper modelMapper, PasswordEncoder ppasswordEncoder, RoleRepo roleRepo) {
        this.repositories = repositories;
        this.modelMapper = modelMapper;
        this.passwordEncoder = ppasswordEncoder;
        this.roleRepo = roleRepo;
    }


    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        Role role = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("role not found"));
        User user = modelMapper.map(request, User.class);
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = repositories.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public List<UserResponseDto> showAllUsers() {
        List<User> allUsers = repositories.findAll();
        return allUsers.stream().map(user -> modelMapper.map(user, UserResponseDto.class)).toList();
    }

    @Override
    public UserResponseDto getUserUsingId(Long id) {
        User user = repositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUserUsingId(UserRequestDto request, Long id) {
        User user = repositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAbout(request.getAbout());
        user.setPassword(request.getPassword());
        User updatedUser = repositories.save((user));
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = repositories.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        repositories.delete(user);
    }
}
