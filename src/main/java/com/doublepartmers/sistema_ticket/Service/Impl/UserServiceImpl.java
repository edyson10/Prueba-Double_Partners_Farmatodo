package com.doublepartmers.sistema_ticket.Service.Impl;

import com.doublepartmers.sistema_ticket.DTO.User.UserDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserRequestDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import com.doublepartmers.sistema_ticket.Exception.NotFoundException;
import com.doublepartmers.sistema_ticket.Mapper.UserMapper;
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
        User user = UserMapper.toEntity(request);
        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new IllegalArgumentException("El email ya está registrado");
        });
        User save = userRepository.save(user);
        return UserMapper.toResponse(save);
    }

    @Override
    public UserResponseDto update(String id, UserRequestDto request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserMapper.updateEntity(existing, request);
        User save = userRepository.save(existing);
        return UserMapper.toResponse(save);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toSummary)
                .toList();
    }

    public User getEntityById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}
