package com.doublepartmers.sistema_ticket.DTO.Ticket;

import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDto(
        String id,
        String descripcion,
        String userId,
        String userNombreCompleto,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        TicketStatus status
) {
}
