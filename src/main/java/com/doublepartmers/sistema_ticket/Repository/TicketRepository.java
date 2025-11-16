package com.doublepartmers.sistema_ticket.Repository;

import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import com.doublepartmers.sistema_ticket.Domain.Model.Ticket;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByUsuario(User usuario, Pageable pageable);

    Page<Ticket> findByStatusAndUsuario(TicketStatus status, User usuario, Pageable pageable);
}
