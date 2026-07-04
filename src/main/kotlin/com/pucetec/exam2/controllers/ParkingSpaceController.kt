package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.services.ParkingSpaceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parking-spaces")
class ParkingSpaceController(
    private val parkingSpaceService: ParkingSpaceService
) {

    @GetMapping("/available")
    fun getAvailableSpaces(): ResponseEntity<List<ParkingSpaceResponse>> =
        ResponseEntity.ok(parkingSpaceService.getAvailableSpaces())
}
