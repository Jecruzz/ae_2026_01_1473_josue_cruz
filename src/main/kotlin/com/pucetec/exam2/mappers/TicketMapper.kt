package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.Ticket
import org.springframework.stereotype.Component

@Component
class TicketMapper {

    fun toResponse(entity: Ticket): TicketResponse =
        TicketResponse(
            id = entity.id!!,
            plate = entity.plate,
            entryTime = entity.entryTime,
            exitTime = entity.exitTime,
            spaceCode = entity.parkingSpace.code
        )
}
