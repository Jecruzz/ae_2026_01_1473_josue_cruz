package com.pucetec.exam2.exceptions

class SpaceAlreadyOccupiedException(code: String) :
    RuntimeException("El espacio '$code' ya está ocupado")
