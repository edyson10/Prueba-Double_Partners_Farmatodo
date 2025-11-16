package com.doublepartmers.sistema_ticket.Mapper;

import com.doublepartmers.sistema_ticket.DTO.User.UserDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserRequestDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Model.User;

public class UserMapper {

    private UserMapper() {}

    public static User toEntity(UserRequestDto dto) {
        return User.builder()
                .nombres(dto.nombres())
                .apellidos(dto.apellidos())
                .email(dto.email())
                .password(dto.password()) // en real → encriptar
                .build();
    }

    public static void updateEntity(User user, UserRequestDto dto) {
        user.setNombres(dto.nombres());
        user.setApellidos(dto.apellidos());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                user.getFechaCreacion(),
                user.getFechaActualizacion()
        );
    }

    public static UserDto toSummary(User user) {
        return new UserDto(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail()
        );
    }
}
