package com.renzozukeram.ecommerce.model.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class OrderItemRequest (
    @field:NotBlank val productName: String,
    @field:Min(1) val quantity: Int,
    @field:Positive val unitPrice: BigDecimal
)