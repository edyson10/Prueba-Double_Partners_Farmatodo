package com.doublepartmers.sistema_ticket.Service.Impl;

import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketRequestDto;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import com.doublepartmers.sistema_ticket.Domain.Model.Ticket;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import com.doublepartmers.sistema_ticket.Exception.NotFoundException;
import com.doublepartmers.sistema_ticket.Mapper.TicketMapper;
import com.doublepartmers.sistema_ticket.Repository.TicketRepository;
import com.doublepartmers.sistema_ticket.Service.TicketService;
import com.doublepartmers.sistema_ticket.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final UserService userService;

    @Override
    public TicketResponseDto create(TicketRequestDto request) {
        User user = ((UserServiceImpl) userService)
                .getEntityById(request.userId());

        Ticket ticket = TicketMapper.toEntity(request, user);
        Ticket save = ticketRepository.save(ticket);
        return TicketMapper.toResponse(save);
    }

    @Override
    public TicketResponseDto update(String id, TicketRequestDto request) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado"));

        User user = ((UserServiceImpl) userService)
                .getEntityById(request.userId());

        TicketMapper.updateEntity(existing, request, user);
        Ticket saved = ticketRepository.save(existing);
        return TicketMapper.toResponse(saved);
    }

    @Override
    public void delete(String id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket no encontrado");
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketResponseDto getById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket no encontrado"));
        return TicketMapper.toResponse(ticket);
    }

    @Override
    public Page<TicketResponseDto> getTickets(TicketStatus status, String userId, Pageable pageable) {
        Page<Ticket> page;

        if (status != null && userId != null) {
            User user = ((UserServiceImpl) userService)
                    .getEntityById(userId);
            page = ticketRepository.findByStatusAndUsuario(status, user, pageable);
        } else if (status != null) {
            page = ticketRepository.findByStatus(status, pageable);
        } else if (userId != null) {
            User user = ((UserServiceImpl) userService)
                    .getEntityById(userId);
            page = ticketRepository.findByUsuario(user, pageable);
        } else {
            page = ticketRepository.findAll(pageable);
        }

        return page.map(TicketMapper::toResponse);
    }
}
