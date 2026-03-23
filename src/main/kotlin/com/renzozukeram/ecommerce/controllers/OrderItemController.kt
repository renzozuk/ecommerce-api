package com.renzozukeram.ecommerce.controllers

import com.renzozukeram.ecommerce.model.dto.OrderItemRequest
import com.renzozukeram.ecommerce.model.dto.OrderItemResponse
import com.renzozukeram.ecommerce.model.dto.OrderItemUpdateRequest
import com.renzozukeram.ecommerce.services.OrderItemService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")
class OrderItemController(private val orderItemService: OrderItemService) {

    @GetMapping("/orders/{orderId}/items")
    fun findByOrderId(@PathVariable orderId: UUID): ResponseEntity<List<OrderItemResponse>> =
        ResponseEntity.ok(orderItemService.findByOrderId(orderId))

    @GetMapping("/order-items/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<OrderItemResponse> =
        ResponseEntity.ok(orderItemService.findById(id))

    @PostMapping("/orders/{orderId}/items")
    fun addItem(
        @PathVariable orderId: UUID,
        @Valid @RequestBody request: OrderItemRequest
    ): ResponseEntity<OrderItemResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(orderItemService.addItem(orderId, request))

    @PutMapping("/order-items/{id}")
    fun updateItem(
        @PathVariable id: UUID,
        @Valid @RequestBody request: OrderItemUpdateRequest
    ): ResponseEntity<OrderItemResponse> =
        ResponseEntity.ok(orderItemService.updateItem(id, request))

    @DeleteMapping("/order-items/{id}")
    fun deleteItem(@PathVariable id: UUID): ResponseEntity<Void> {
        orderItemService.deleteItem(id)
        return ResponseEntity.noContent().build()
    }
}