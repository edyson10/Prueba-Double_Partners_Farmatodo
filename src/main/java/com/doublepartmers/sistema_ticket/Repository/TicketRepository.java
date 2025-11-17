package com.doublepartmers.sistema_ticket.Repository;

import com.doublepartmers.sistema_ticket.Domain.Enums.TicketStatus;
import com.doublepartmers.sistema_ticket.Domain.Model.Ticket;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByUsuario(User usuario, Pageable pageable);

    Page<Ticket> findByStatusAndUsuario(TicketStatus status, User usuario, Pageable pageable);

    @Query("""
    SELECT t FROM Ticket t
    JOIN t.usuario u
    WHERE (:status IS NULL OR t.status = :status)
      AND (:descripcion IS NULL OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
      AND (:userId IS NULL OR u.id = :userId)
      AND (:email IS NULL OR LOWER(u.email) = LOWER(:email))
      AND (:fechaDesde IS NULL OR t.fechaCreacion >= CAST(:fechaDesde AS date))
      AND (:fechaHasta IS NULL OR t.fechaCreacion <= CAST(:fechaHasta AS date))
""")
    List<Ticket> filtrarTickets(
            @Param("status") TicketStatus status,
            @Param("descripcion") String descripcion,
            @Param("userId") String userId,
            @Param("email") String email,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );
}
