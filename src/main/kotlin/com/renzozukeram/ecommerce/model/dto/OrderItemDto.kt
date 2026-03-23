package com.renzozukeram.ecommerce.model.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.util.*

data class OrderItemRequest(
    @field:NotBlank(message = "Product name is required")
    val productName: String,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int,

    @field:NotNull(message = "Unit price is required")
    @field:Positive(message = "Unit price must be positive")
    val unitPrice: BigDecimal
)

data class OrderItemUpdateRequest(
    @field:NotBlank(message = "Product name is required")
    val productName: String,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int,

    @field:NotNull(message = "Unit price is required")
    @field:Positive(message = "Unit price must be positive")
    val unitPrice: BigDecimal
)

data class OrderItemResponse(
    val id: UUID,
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal
)