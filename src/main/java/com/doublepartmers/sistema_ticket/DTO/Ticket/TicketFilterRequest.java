package com.doublepartmers.sistema_ticket.DTO.Ticket;

import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;

public record TicketFilterRequest(
        TicketStatus status,
        String userId
) {
}
