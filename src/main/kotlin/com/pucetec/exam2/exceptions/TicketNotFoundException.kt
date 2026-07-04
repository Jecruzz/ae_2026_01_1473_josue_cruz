package com.pucetec.exam2.exceptions

class TicketNotFoundException(plate: String) :
    RuntimeException("No existe un ticket abierto para la placa '$plate'")
