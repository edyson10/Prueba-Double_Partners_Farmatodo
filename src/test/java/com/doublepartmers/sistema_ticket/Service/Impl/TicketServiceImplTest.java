package com.doublepartmers.sistema_ticket.Service.Impl;

import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketFilterRequest;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketRequestDto;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import com.doublepartmers.sistema_ticket.Domain.Model.Ticket;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import com.doublepartmers.sistema_ticket.Exception.NotFoundException;
import com.doublepartmers.sistema_ticket.Mapper.TicketMapper;
import com.doublepartmers.sistema_ticket.Repository.TicketRepository;
import com.doublepartmers.sistema_ticket.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User mockUser;
    private Ticket mockTicket;
    private TicketResponseDto mockResponse;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setNombres("Usuario");
        mockUser.setApellidos("Prueba");
        mockUser.setEmail("edyson@double.com");

        mockTicket = new Ticket();
        mockTicket.setId("ticket123");
        mockTicket.setDescripcion("Mi primer ticket de prueba");
        mockTicket.setUsuario(mockUser);
        mockTicket.setStatus(TicketStatus.ABIERTO);
        mockTicket.setFechaCreacion(LocalDateTime.now());
        mockTicket.setFechaActualizacion(LocalDateTime.now());

        mockResponse = new TicketResponseDto(
                "ticket123",
                "Mi primer ticket de prueba",
                "user123",
                "Usuario Prueba",
                mockTicket.getFechaCreacion(),
                mockTicket.getFechaActualizacion(),
                TicketStatus.ABIERTO
        );
    }

    @Test
    void create() {
        TicketRequestDto request = new TicketRequestDto(
                "Nuevo ticket",
                "user123",
                TicketStatus.ABIERTO
        );

        when(userService.getEntityById("user123")).thenReturn(mockUser);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);

        try (MockedStatic<TicketMapper> mockedMapper = Mockito.mockStatic(TicketMapper.class)) {

            mockedMapper.when(() -> TicketMapper.toEntity(request, mockUser))
                    .thenReturn(mockTicket);

            mockedMapper.when(() -> TicketMapper.toResponse(mockTicket))
                    .thenReturn(new TicketResponseDto(
                            "ticket123",             // id
                            "Nuevo ticket",          // descripcion
                            "user123",               // userId
                            "Usuario Prueba",        // userNombreCompleto
                            LocalDateTime.now(),     // fechaCreacion
                            LocalDateTime.now(),     // fechaActualizacion
                            TicketStatus.ABIERTO
                    ));

            TicketResponseDto response = ticketService.create(request);

            assertEquals("ticket123", response.id());
            verify(ticketRepository, times(1)).save(mockTicket);
        }
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------
    @Test
    void update() {
        TicketRequestDto request = new TicketRequestDto(
                "Actualizado",
                "user123",
                TicketStatus.CERRADO
        );

        when(ticketRepository.findById("ticket123")).thenReturn(Optional.of(mockTicket));
        when(userService.getEntityById("user123")).thenReturn(mockUser);
        when(ticketRepository.save(mockTicket)).thenReturn(mockTicket);

        Ticket updated = mockTicket;
        updated.setDescripcion("Actualizado");
        updated.setStatus(TicketStatus.CERRADO);

        TicketResponseDto updatedResponse = new TicketResponseDto(
                "ticket123",
                "Actualizado",
                "user123",
                "Usuario Prueba",
                LocalDateTime.now(),
                LocalDateTime.now(),
                TicketStatus.CERRADO
        );

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {

            mapperMock.when(() -> TicketMapper.updateEntity(mockTicket, request, mockUser))
                    .then(inv -> null);

            mapperMock.when(() -> TicketMapper.toResponse(mockTicket))
                    .thenReturn(updatedResponse);

            TicketResponseDto result = ticketService.update("ticket123", request);

            assertEquals("Actualizado", result.descripcion());
            assertEquals(TicketStatus.CERRADO, result.status());
        }
    }

    @Test
    void update_ShouldThrowNotFound() {
        when(ticketRepository.findById("badId")).thenReturn(Optional.empty());
        TicketRequestDto request = new TicketRequestDto(
                "Nuevo",
                "user123",
                TicketStatus.ABIERTO
        );

        assertThrows(NotFoundException.class, () -> ticketService.update("badId", request));
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    @Test
    void delete() {
        when(ticketRepository.existsById("ticket123")).thenReturn(true);

        ticketService.delete("ticket123");

        verify(ticketRepository).deleteById("ticket123");
    }

    @Test
    void delete_ShouldThrowNotFound() {
        when(ticketRepository.existsById("badId")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ticketService.delete("badId"));
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Test
    void getById() {
        when(ticketRepository.findById("ticket123")).thenReturn(Optional.of(mockTicket));

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(mockTicket))
                    .thenReturn(mockResponse);

            TicketResponseDto result = ticketService.getById("ticket123");

            assertEquals("ticket123", result.id());
        }
    }

    @Test
    void getById_ShouldThrowWhenNotFound() {
        when(ticketRepository.findById("badId")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> ticketService.getById("badId"));
    }

    // ---------------------------------------------------------
    // GET TICKETS (paginate)
    // ---------------------------------------------------------
    @Test
    void getTickets() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(List.of(mockTicket));

        when(ticketRepository.findAll(pageable)).thenReturn(page);

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(mockTicket))
                    .thenReturn(mockResponse);

            Page<TicketResponseDto> result = ticketService.getTickets(null, null, pageable);

            assertEquals(1, result.getTotalElements());
        }
    }

    // ---------------------------------------------------------
    // FILTRAR TICKETS
    // ---------------------------------------------------------
    @Test
    void filtrarTickets() {

        TicketFilterRequest filter = new TicketFilterRequest(
                TicketStatus.ABIERTO,
                null,
                null,
                null,
                null,
                null
        );

        when(ticketRepository.filtrarTickets(
                filter.status(),
                filter.descripcion(),
                filter.userId(),
                filter.email(),
                filter.fechaDesde(),
                filter.fechaHasta()
        )).thenReturn(List.of(mockTicket));

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {

            mapperMock.when(() -> TicketMapper.toResponse(mockTicket))
                    .thenReturn(mockResponse);

            List<TicketResponseDto> result = ticketService.filtrarTickets(filter);

            assertEquals(1, result.size());
            assertEquals("ticket123", result.get(0).id());
        }
    }
}