package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.dto.ExitRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.services.TicketService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController(
    private val ticketService: TicketService
) {

    @PostMapping("/entry")
    fun registerEntry(@Valid @RequestBody request: EntryRequest): ResponseEntity<TicketResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(ticketService.registerEntry(request))

    @PostMapping("/exit")
    fun registerExit(@Valid @RequestBody request: ExitRequest): ResponseEntity<TicketResponse> =
        ResponseEntity.ok(ticketService.registerExit(request))
}
