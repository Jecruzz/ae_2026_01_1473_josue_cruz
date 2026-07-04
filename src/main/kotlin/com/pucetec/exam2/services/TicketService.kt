package com.pucetec.exam2.services

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.dto.ExitRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.ParkingSpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceAlreadyOccupiedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.mappers.TicketMapper
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val parkingSpaceRepository: ParkingSpaceRepository,
    private val ticketMapper: TicketMapper
) {

    private val logger = LoggerFactory.getLogger(TicketService::class.java)

    fun registerEntry(request: EntryRequest): TicketResponse {
        logger.info("Registrando entrada para la placa {} en el espacio {}", request.plate, request.spaceCode)

        val parkingSpace = parkingSpaceRepository.findByCode(request.spaceCode)
            .orElseThrow { ParkingSpaceNotFoundException(request.spaceCode) }

        if (parkingSpace.occupied) {
            logger.warn("El espacio {} ya está ocupado", request.spaceCode)
            throw SpaceAlreadyOccupiedException(request.spaceCode)
        }

        parkingSpace.occupied = true
        parkingSpaceRepository.save(parkingSpace)

        val ticket = Ticket(
            plate = request.plate,
            entryTime = LocalDateTime.now(),
            exitTime = null,
            parkingSpace = parkingSpace
        )
        val savedTicket = ticketRepository.save(ticket)

        logger.info("Entrada registrada. Ticket {} creado", savedTicket.id)
        return ticketMapper.toResponse(savedTicket)
    }

    fun registerExit(request: ExitRequest): TicketResponse {
        logger.info("Registrando salida para la placa {}", request.plate)

        val ticket = ticketRepository.findByPlateAndExitTimeIsNull(request.plate)
            .orElseThrow { TicketNotFoundException(request.plate) }

        ticket.exitTime = LocalDateTime.now()
        ticket.parkingSpace.occupied = false
        parkingSpaceRepository.save(ticket.parkingSpace)
        val savedTicket = ticketRepository.save(ticket)

        logger.info("Salida registrada. Ticket {} cerrado", savedTicket.id)
        return ticketMapper.toResponse(savedTicket)
    }
}
