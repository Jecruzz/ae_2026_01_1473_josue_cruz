package com.pucetec.exam2.exceptions

class ParkingSpaceNotFoundException(code: String) :
    RuntimeException("No existe un espacio de estacionamiento con el código '$code'")
