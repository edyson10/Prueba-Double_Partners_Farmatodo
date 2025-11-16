package com.doublepartmers.sistema_ticket.Service.Impl;

import com.doublepartmers.sistema_ticket.DTO.User.UserDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserRequestDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserResponseDto;
import com.doublepartmers.sistema_ticket.Repository.UserRepository;
import com.doublepartmers.sistema_ticket.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserResponseDto create(UserRequestDto request) {
        return null;
    }

    @Override
    public UserResponseDto update(String id, UserRequestDto request) {
        return null;
    }

    @Override
    public UserResponseDto getById(String id) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }
}
