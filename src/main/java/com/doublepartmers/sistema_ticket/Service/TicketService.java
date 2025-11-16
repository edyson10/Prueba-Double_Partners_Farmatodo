package com.doublepartmers.sistema_ticket.Service;

import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketRequestDto;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketResponseDto create(TicketRequestDto request);
    TicketResponseDto update(String id, TicketRequestDto request);
    void delete(String id);
    TicketResponseDto getById(String id);
    Page<TicketResponseDto> getTickets(TicketStatus status, String userId, Pageable pageable);
}
