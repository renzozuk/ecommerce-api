package com.renzozukeram.ecommerce.model.dto.response

import java.util.*

data class CustomerSummaryResponse (
    val id: UUID,
    val name: String,
    val email: String
)