package com.renzozukeram.ecommerce.controllers

import com.renzozukeram.ecommerce.model.dto.OrderRequest
import com.renzozukeram.ecommerce.model.dto.OrderResponse
import com.renzozukeram.ecommerce.model.dto.OrderUpdateRequest
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import com.renzozukeram.ecommerce.services.OrderService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun findAll(
        @PageableDefault(size = 20, sort = ["orderDate"]) pageable: Pageable
    ): ResponseEntity<Page<OrderResponse>> =
        ResponseEntity.ok(orderService.findAll(pageable))

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.findById(id))

    @GetMapping("/customer/{customerId}")
    fun findByCustomerId(
        @PathVariable customerId: UUID,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<OrderResponse>> =
        ResponseEntity.ok(orderService.findByCustomerId(customerId, pageable))

    @GetMapping("/status/{status}")
    fun findByStatus(
        @PathVariable status: OrderStatus,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<OrderResponse>> =
        ResponseEntity.ok(orderService.findByStatus(status, pageable))

    @PostMapping
    fun create(@Valid @RequestBody request: OrderRequest): ResponseEntity<OrderResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request))

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: OrderUpdateRequest
    ): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.updateStatus(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        orderService.delete(id)
        return ResponseEntity.noContent().build()
    }
}