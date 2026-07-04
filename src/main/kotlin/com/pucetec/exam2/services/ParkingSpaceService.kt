package com.pucetec.exam2.services

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.mappers.ParkingSpaceMapper
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParkingSpaceService(
    private val parkingSpaceRepository: ParkingSpaceRepository,
    private val parkingSpaceMapper: ParkingSpaceMapper
) {

    private val logger = LoggerFactory.getLogger(ParkingSpaceService::class.java)

    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        logger.info("Consultando espacios disponibles")
        val available = parkingSpaceRepository.findByOccupiedFalse()
        return parkingSpaceMapper.toResponseList(available)
    }
}
