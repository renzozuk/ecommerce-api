package com.renzozukeram.ecommerce.model.dto.response

import java.math.BigDecimal

data class OrderItemResponse (
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal
)