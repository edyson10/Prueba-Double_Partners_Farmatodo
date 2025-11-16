package com.doublepartmers.sistema_ticket.DTO.Ticket;

import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketRequestDto(
        @NotBlank @Size(max = 500) String descripcion,
        @NotBlank String userId,
        @NotNull TicketStatus status
) {
}
