package com.doublepartmers.sistema_ticket.Service;

import com.doublepartmers.sistema_ticket.DTO.User.UserDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserRequestDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRequestDto request);
    UserResponseDto update(String id, UserRequestDto request);
    UserResponseDto getById(String id);
    List<UserDto> getAll();
}
