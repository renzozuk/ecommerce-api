package com.renzozukeram.ecommerce.mappers

import com.renzozukeram.ecommerce.model.dto.request.AddressRequest
import com.renzozukeram.ecommerce.model.dto.request.CustomerRequest
import com.renzozukeram.ecommerce.model.dto.response.AddressResponse
import com.renzozukeram.ecommerce.model.dto.response.CustomerResponse
import com.renzozukeram.ecommerce.model.entities.Address
import com.renzozukeram.ecommerce.model.entities.Customer
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomerMapper {

    fun toEntity(request: CustomerRequest): Customer {
        return Customer(
            name = request.name,
            email = request.email,
            phoneNumber = request.phoneNumber,
            address = request.address?.let { toAddressEntity(it) }
        )
    }

    fun toResponse(customer: Customer, includeOrdersCount: Boolean = false): CustomerResponse {
        return CustomerResponse(
            id = customer.id!!,
            name = customer.name,
            email = customer.email,
            phoneNumber = customer.phoneNumber,
            address = customer.address?.let { toAddressResponse(it) },
            createdAt = customer.createdAt,
            ordersCount = if (includeOrdersCount) customer.orders.size else null
        )
    }

    fun toAddressEntity(request: AddressRequest): Address {
        return Address(
            street = request.street,
            city = request.city,
            state = request.state,
            zipCode = request.zipCode,
            complement = request.complement
        )
    }

    fun toAddressResponse(address: Address): AddressResponse {
        return AddressResponse(
            street = address.street,
            city = address.city,
            state = address.state,
            zipCode = address.zipCode,
            complement = address.complement
        )
    }

    fun updateEntity(customer: Customer, request: CustomerRequest) {
        customer.name = request.name
        customer.email = request.email
        customer.phoneNumber = request.phoneNumber
        request.address?.let {
            customer.address = toAddressEntity(it)
        }
        customer.updatedAt = LocalDateTime.now()
    }
}