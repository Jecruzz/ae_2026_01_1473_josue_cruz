package com.pucetec.exam2.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ParkingSpaceNotFoundException::class)
    fun handleParkingSpaceNotFound(ex: ParkingSpaceNotFoundException): ResponseEntity<ErrorResponse> =
        buildResponse(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(ex: TicketNotFoundException): ResponseEntity<ErrorResponse> =
        buildResponse(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(SpaceAlreadyOccupiedException::class)
    fun handleSpaceAlreadyOccupied(ex: SpaceAlreadyOccupiedException): ResponseEntity<ErrorResponse> =
        buildResponse(HttpStatus.CONFLICT, ex.message)

    private fun buildResponse(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = message
        )
        return ResponseEntity.status(status).body(body)
    }
}
