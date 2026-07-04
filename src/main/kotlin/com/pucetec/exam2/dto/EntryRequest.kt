package com.pucetec.exam2.dto

import jakarta.validation.constraints.NotBlank

data class EntryRequest(
    @field:NotBlank(message = "La placa es obligatoria")
    val plate: String,

    @field:NotBlank(message = "El código del espacio es obligatorio")
    val spaceCode: String
)
