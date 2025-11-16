package com.doublepartmers.sistema_ticket.Mapper;

import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketRequestDto;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Model.Ticket;
import com.doublepartmers.sistema_ticket.Domain.Model.User;

public class TicketMapper {

    private TicketMapper() {}

    public static Ticket toEntity(TicketRequestDto dto, User user) {
        return Ticket.builder()
                .descripcion(dto.descripcion())
                .usuario(user)
                .status(dto.status())
                .build();
    }

    public static void updateEntity(Ticket ticket, TicketRequestDto dto, User user) {
        ticket.setDescripcion(dto.descripcion());
        ticket.setUsuario(user);
        ticket.setStatus(dto.status());
    }

    public static TicketResponseDto toResponse(Ticket ticket) {
        User u = ticket.getUsuario();
        String nombreCompleto = u.getNombres() + " " + u.getApellidos();
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getDescripcion(),
                u.getId(),
                nombreCompleto,
                ticket.getFechaCreacion(),
                ticket.getFechaActualizacion(),
                ticket.getStatus()
        );
    }
}
