package com.renzozukeram.ecommerce.mappers

import com.renzozukeram.ecommerce.model.dto.CustomerRequest
import com.renzozukeram.ecommerce.model.dto.CustomerResponse
import com.renzozukeram.ecommerce.model.dto.CustomerUpdateRequest
import com.renzozukeram.ecommerce.model.dto.CustomerWithOrdersResponse
import com.renzozukeram.ecommerce.model.entities.Customer
import org.springframework.stereotype.Component

@Component
class CustomerMapper(private val orderMapper: OrderMapper) {

    fun toEntity(request: CustomerRequest): Customer =
        Customer(
            name = request.name,
            email = request.email,
            phoneNumber = request.phoneNumber
        )

    fun toResponse(customer: Customer): CustomerResponse =
        CustomerResponse(
            id = customer.id!!,
            name = customer.name,
            email = customer.email,
            phoneNumber = customer.phoneNumber,
            createdAt = customer.createdAt,
            updatedAt = customer.updatedAt
        )

    fun toResponseWithOrders(customer: Customer): CustomerWithOrdersResponse =
        CustomerWithOrdersResponse(
            id = customer.id!!,
            name = customer.name,
            email = customer.email,
            phoneNumber = customer.phoneNumber,
            createdAt = customer.createdAt,
            updatedAt = customer.updatedAt,
            orders = customer.orders.map { orderMapper.toSummaryResponse(it) }
        )

    fun updateEntity(customer: Customer, request: CustomerUpdateRequest): Customer {
        customer.name = request.name
        customer.email = request.email
        customer.phoneNumber = request.phoneNumber
        customer.updatedAt = java.time.LocalDateTime.now()
        return customer
    }
}
