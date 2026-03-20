package com.renzozukeram.ecommerce.model.dto.response

data class AddressResponse (
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val complement: String?
)