package com.renzozukeram.ecommerce.model.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class CustomerResponse (
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val address: AddressResponse?,
    val createdAt: LocalDateTime,
    val ordersCount: Int? = null,
    val links: Map<String, String>? = null
)