package com.renzozukeram.ecommerce.model.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Address (
    @Column(nullable = false)
    var street: String = "",

    @Column(nullable = false)
    var city: String = "",

    @Column(nullable = false)
    var state: String = "",

    @Column(name = "zip_code", nullable = false)
    var zipCode: String = "",

    var complement: String? = null
)