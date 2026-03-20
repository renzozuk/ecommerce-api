package com.renzozukeram.ecommerce.model.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class CustomerRequest (
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:Pattern(regexp = "^\\+[1-9]\\d{1,14}\$", message = "Invalid phone number")
    val phoneNumber: String? = null,

    @field:Valid
    val address: AddressRequest? = null
)