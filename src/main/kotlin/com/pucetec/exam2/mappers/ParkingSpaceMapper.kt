package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.entities.ParkingSpace
import org.springframework.stereotype.Component

@Component
class ParkingSpaceMapper {

    fun toResponse(entity: ParkingSpace): ParkingSpaceResponse =
        ParkingSpaceResponse(
            id = entity.id!!,
            code = entity.code,
            occupied = entity.occupied
        )

    fun toResponseList(entities: List<ParkingSpace>): List<ParkingSpaceResponse> =
        entities.map { toResponse(it) }
}
