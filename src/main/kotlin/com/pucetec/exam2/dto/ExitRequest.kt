package com.pucetec.exam2.dto

import jakarta.validation.constraints.NotBlank

data class ExitRequest(
    @field:NotBlank(message = "La placa es obligatoria")
    val plate: String
)
