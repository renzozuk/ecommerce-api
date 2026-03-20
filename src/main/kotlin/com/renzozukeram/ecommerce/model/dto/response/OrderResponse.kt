package com.renzozukeram.ecommerce.model.dto.response

import com.renzozukeram.ecommerce.model.entities.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class OrderResponse (
    val id: UUID,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val items: List<OrderItemResponse>,
    val customerSummary: CustomerSummaryResponse
)