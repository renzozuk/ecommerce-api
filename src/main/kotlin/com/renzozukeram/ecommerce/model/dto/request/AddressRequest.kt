package com.renzozukeram.ecommerce.model.dto.request

import jakarta.validation.constraints.NotBlank

data class AddressRequest (
    @field:NotBlank val street: String,
    @field:NotBlank val city: String,
    @field:NotBlank val state: String,
    @field:NotBlank val zipCode: String,
    val complement: String? = null
)