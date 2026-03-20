package com.renzozukeram.ecommerce.model.dto.request

import jakarta.validation.Valid
import java.util.*

data class OrderRequest (
    @field:Valid
    val items: List<OrderItemRequest>,

    val customerId: UUID? = null
)