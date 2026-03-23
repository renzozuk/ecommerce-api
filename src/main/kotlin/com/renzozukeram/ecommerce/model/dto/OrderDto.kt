package com.renzozukeram.ecommerce.model.dto

import com.renzozukeram.ecommerce.model.entities.OrderStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class OrderRequest(
    @field:NotNull(message = "Customer ID is required")
    val customerId: UUID,

    @field:NotEmpty(message = "Order must have at least one item")
    @field:Valid
    val items: List<OrderItemRequest>
)

data class OrderUpdateRequest(
    @field:NotNull(message = "Status is required")
    val status: OrderStatus
)

data class OrderResponse(
    val id: UUID,
    val customerId: UUID,
    val customerName: String,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val items: List<OrderItemResponse>,
    val createdAt: LocalDateTime
)

data class OrderSummaryResponse(
    val id: UUID,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val createdAt: LocalDateTime
)