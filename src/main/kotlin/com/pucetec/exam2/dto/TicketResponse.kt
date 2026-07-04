package com.pucetec.exam2.dto

import java.time.LocalDateTime

data class TicketResponse(
    val id: Long,
    val plate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val spaceCode: String
)
