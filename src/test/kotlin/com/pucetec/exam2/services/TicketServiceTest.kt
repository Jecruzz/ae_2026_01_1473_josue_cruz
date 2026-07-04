package com.pucetec.exam2.services

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.dto.ExitRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.ParkingSpace
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.ParkingSpaceNotFoundException
import com.pucetec.exam2.exceptions.SpaceAlreadyOccupiedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.mappers.TicketMapper
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class TicketServiceTest {

    @Mock
    private lateinit var ticketRepository: TicketRepository

    @Mock
    private lateinit var parkingSpaceRepository: ParkingSpaceRepository

    @Mock
    private lateinit var ticketMapper: TicketMapper

    @InjectMocks
    private lateinit var ticketService: TicketService

    @Test
    fun `registerEntry should create ticket when space is available`() {
        val request = EntryRequest(plate = "ABC-123", spaceCode = "P1")
        val parkingSpace = ParkingSpace(id = 1L, code = "P1", occupied = false)
        val savedSpace = ParkingSpace(id = 1L, code = "P1", occupied = true)
        val ticket = Ticket(
            plate = "ABC-123",
            entryTime = LocalDateTime.now(),
            exitTime = null,
            parkingSpace = parkingSpace
        )
        val savedTicket = Ticket(
            id = 1L,
            plate = "ABC-123",
            entryTime = ticket.entryTime,
            exitTime = null,
            parkingSpace = savedSpace
        )
        val expectedResponse = TicketResponse(
            id = 1L,
            plate = "ABC-123",
            entryTime = savedTicket.entryTime,
            exitTime = null,
            spaceCode = "P1"
        )

        whenever(parkingSpaceRepository.findByCode("P1")).thenReturn(Optional.of(parkingSpace))
        whenever(parkingSpaceRepository.save(any<ParkingSpace>())).thenReturn(savedSpace)
        whenever(ticketRepository.save(any<Ticket>())).thenReturn(savedTicket)
        whenever(ticketMapper.toResponse(savedTicket)).thenReturn(expectedResponse)

        val result = ticketService.registerEntry(request)

        assertNotNull(result)
        assertEquals(expectedResponse, result)
        assertEquals(true, parkingSpace.occupied)
        verify(parkingSpaceRepository).findByCode("P1")
        verify(parkingSpaceRepository).save(parkingSpace)
        verify(ticketRepository).save(any<Ticket>())
        verify(ticketMapper).toResponse(savedTicket)
    }

    @Test
    fun `registerEntry should throw ParkingSpaceNotFoundException when space does not exist`() {
        val request = EntryRequest(plate = "ABC-123", spaceCode = "NONEXISTENT")

        whenever(parkingSpaceRepository.findByCode("NONEXISTENT")).thenReturn(Optional.empty())

        val exception = assertThrows(ParkingSpaceNotFoundException::class.java) {
            ticketService.registerEntry(request)
        }

        assertEquals("No existe un espacio de estacionamiento con el c\u00f3digo 'NONEXISTENT'", exception.message)
        verify(parkingSpaceRepository).findByCode("NONEXISTENT")
    }

    @Test
    fun `registerEntry should throw SpaceAlreadyOccupiedException when space is occupied`() {
        val request = EntryRequest(plate = "ABC-123", spaceCode = "P1")
        val parkingSpace = ParkingSpace(id = 1L, code = "P1", occupied = true)

        whenever(parkingSpaceRepository.findByCode("P1")).thenReturn(Optional.of(parkingSpace))

        val exception = assertThrows(SpaceAlreadyOccupiedException::class.java) {
            ticketService.registerEntry(request)
        }

        assertEquals("El espacio 'P1' ya est\u00e1 ocupado", exception.message)
        verify(parkingSpaceRepository).findByCode("P1")
    }

    @Test
    fun `registerExit should close ticket and free space when ticket exists`() {
        val request = ExitRequest(plate = "ABC-123")
        val parkingSpace = ParkingSpace(id = 1L, code = "P1", occupied = true)
        val ticket = Ticket(
            id = 1L,
            plate = "ABC-123",
            entryTime = LocalDateTime.now().minusHours(2),
            exitTime = null,
            parkingSpace = parkingSpace
        )
        val closedTicket = Ticket(
            id = 1L,
            plate = "ABC-123",
            entryTime = ticket.entryTime,
            exitTime = LocalDateTime.now(),
            parkingSpace = parkingSpace
        )
        val expectedResponse = TicketResponse(
            id = 1L,
            plate = "ABC-123",
            entryTime = ticket.entryTime,
            exitTime = closedTicket.exitTime,
            spaceCode = "P1"
        )

        whenever(ticketRepository.findByPlateAndExitTimeIsNull("ABC-123")).thenReturn(Optional.of(ticket))
        whenever(parkingSpaceRepository.save(parkingSpace)).thenReturn(parkingSpace)
        whenever(ticketRepository.save(ticket)).thenReturn(closedTicket)
        whenever(ticketMapper.toResponse(closedTicket)).thenReturn(expectedResponse)

        val result = ticketService.registerExit(request)

        assertNotNull(result)
        assertEquals(expectedResponse, result)
        assertEquals(false, parkingSpace.occupied)
        assertNotNull(ticket.exitTime)
        verify(ticketRepository).findByPlateAndExitTimeIsNull("ABC-123")
        verify(parkingSpaceRepository).save(parkingSpace)
        verify(ticketRepository).save(ticket)
        verify(ticketMapper).toResponse(closedTicket)
    }

    @Test
    fun `registerExit should throw TicketNotFoundException when no open ticket exists`() {
        val request = ExitRequest(plate = "NO-TICKET")

        whenever(ticketRepository.findByPlateAndExitTimeIsNull("NO-TICKET")).thenReturn(Optional.empty())

        val exception = assertThrows(TicketNotFoundException::class.java) {
            ticketService.registerExit(request)
        }

        assertEquals("No existe un ticket abierto para la placa 'NO-TICKET'", exception.message)
        verify(ticketRepository).findByPlateAndExitTimeIsNull("NO-TICKET")
    }
}
