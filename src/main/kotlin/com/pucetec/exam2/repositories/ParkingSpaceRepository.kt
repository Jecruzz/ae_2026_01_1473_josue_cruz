package com.pucetec.exam2.repositories

import com.pucetec.exam2.entities.ParkingSpace
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ParkingSpaceRepository : JpaRepository<ParkingSpace, Long> {

    fun findByCode(code: String): Optional<ParkingSpace>

    fun findByOccupiedFalse(): List<ParkingSpace>
}
