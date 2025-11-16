package com.doublepartmers.sistema_ticket.DTO.User;

import java.time.LocalDateTime;

public record UserResponseDto(
        String id,
        String nombres,
        String apellidos,
        String email,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
