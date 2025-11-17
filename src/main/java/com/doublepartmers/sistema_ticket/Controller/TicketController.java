package com.doublepartmers.sistema_ticket.Controller;

import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketFilterRequest;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketRequestDto;
import com.doublepartmers.sistema_ticket.DTO.Ticket.TicketResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import com.doublepartmers.sistema_ticket.Service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets/")
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    private final TicketService ticketService;

    @PostMapping("crear-ticket")
    public ResponseEntity<TicketResponseDto> create(@Valid @RequestBody TicketRequestDto request) {
        TicketResponseDto response = ticketService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<TicketResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody TicketRequestDto request) {
        return ResponseEntity.ok(ticketService.update(id, request));
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("obtener/{id}")
    public ResponseEntity<TicketResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping("listar")
    public ResponseEntity<Page<TicketResponseDto>> list(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return ResponseEntity.ok(ticketService.getTickets(status, userId, pageable));
    }

    @PostMapping("filter")
    public ResponseEntity<List<TicketResponseDto>> filtrarTickets(
            @RequestBody TicketFilterRequest filter) {

        return ResponseEntity.ok(ticketService.filtrarTickets(filter));
    }
}
