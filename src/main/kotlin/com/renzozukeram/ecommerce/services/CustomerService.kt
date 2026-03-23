package com.renzozukeram.ecommerce.services

import com.renzozukeram.ecommerce.mappers.CustomerMapper
import com.renzozukeram.ecommerce.mappers.OrderMapper
import com.renzozukeram.ecommerce.model.dto.request.CustomerRequest
import com.renzozukeram.ecommerce.model.dto.response.CustomerResponse
import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.repositories.CustomerRepository
import com.renzozukeram.ecommerce.repositories.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val customerMapper: CustomerMapper,
    private val orderMapper: OrderMapper
) {

    fun findAll(): Iterable<CustomerResponse> {
        return customerRepository.findAll().map { c -> customerMapper.toResponse(c) }
    }

    fun findById(id: UUID): CustomerResponse {
        return customerMapper.toResponse(customerRepository.findById(id)
            .orElseThrow { RuntimeException("Customer not found with id: $id") })
    }

    fun findByEmail(email: String): CustomerResponse? {
        return customerRepository.findByEmail(email)?.let { customerMapper.toResponse(it) }
    }

    @Transactional
    fun create(customerRequest: CustomerRequest): CustomerResponse {
        validateEmail(customerRequest.email)
        return customerMapper.toResponse(customerRepository.save(customerMapper.toEntity(customerRequest)))
    }

    @Transactional
    fun update(id: UUID, customerRequest: CustomerRequest): CustomerResponse {
        val existingCustomer = customerRepository.findById(id).orElseThrow { RuntimeException("Customer not found with id: $id") }

        existingCustomer.name = customerRequest.name
        existingCustomer.phoneNumber = customerRequest.phoneNumber
        customerRequest.address?.let {
            existingCustomer.address = orderMapper.toAddressEntity(it)
        }

        if (existingCustomer.email != customerRequest.email) {
            validateEmail(customerRequest.email)
            existingCustomer.email = customerRequest.email
        }

        existingCustomer.updatedAt = java.time.LocalDateTime.now()

        return customerMapper.toResponse(customerRepository.save(existingCustomer))
    }

    @Transactional
    fun delete(id: UUID) {
        val customer = findById(id)

        if (customer.ordersCount != 0) {
            throw RuntimeException("Cannot delete customer with existing orders")
        }

        customerRepository.deleteById(id)
    }

    fun findOrdersByCustomerId(customerId: UUID): List<Order> {
        if (!customerRepository.existsById(customerId)) {
            throw RuntimeException("Customer not found with id: $customerId")
        }

        return orderRepository.findByCustomerId(customerId)
    }

    private fun validateEmail(email: String) {
        if (customerRepository.existsByEmail(email)) {
            throw RuntimeException("Email already exists: $email")
        }
    }

//    fun existsById(id: UUID): Boolean {
//        return customerRepository.existsById(id)
//    }
}