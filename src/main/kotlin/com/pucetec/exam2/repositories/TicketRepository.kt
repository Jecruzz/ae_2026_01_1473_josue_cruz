package com.pucetec.exam2.repositories

import com.pucetec.exam2.entities.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findByPlateAndExitTimeIsNull(plate: String): Optional<Ticket>
}
