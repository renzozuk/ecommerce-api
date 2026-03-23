package com.renzozukeram.ecommerce.model.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime
import java.util.*

data class CustomerRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    val phoneNumber: String? = null
)

data class CustomerUpdateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    val phoneNumber: String? = null
)

data class CustomerResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

data class CustomerWithOrdersResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val orders: List<OrderSummaryResponse>
)