package com.renzozukeram.ecommerce.services

import com.renzozukeram.ecommerce.model.entities.Customer
import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.repositories.CustomerRepository
import com.renzozukeram.ecommerce.repositories.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository
) {

    fun findAll(): Iterable<Customer> {
        return customerRepository.findAll()
    }

    fun findById(id: UUID): Customer {
        return customerRepository.findById(id)
            .orElseThrow { RuntimeException("Customer not found with id: $id") }
    }

    fun findByEmail(email: String): Customer? {
        return customerRepository.findByEmail(email)
    }

    @Transactional
    fun create(customer: Customer): Customer {
        validateEmail(customer.email)
        return customerRepository.save(customer)
    }

    @Transactional
    fun update(id: UUID, customerUpdates: Customer): Customer {
        val existingCustomer = findById(id)

        existingCustomer.name = customerUpdates.name
        existingCustomer.phoneNumber = customerUpdates.phoneNumber
        customerUpdates.address?.let {
            existingCustomer.address = it
        }

        if (existingCustomer.email != customerUpdates.email) {
            validateEmail(customerUpdates.email)
            existingCustomer.email = customerUpdates.email
        }

        existingCustomer.updatedAt = java.time.LocalDateTime.now()

        return customerRepository.save(existingCustomer)
    }

    @Transactional
    fun delete(id: UUID) {
        val customer = findById(id)

        if (customer.orders.isNotEmpty()) {
            throw RuntimeException("Cannot delete customer with existing orders")
        }

        customerRepository.delete(customer)
    }

    fun findOrdersByCustomerId(customerId: UUID, pageable: Pageable): Page<Order> {
        if (!customerRepository.existsById(customerId)) {
            throw RuntimeException("Customer not found with id: $customerId")
        }

        return orderRepository.findByCustomerId(customerId, pageable)
    }

    private fun validateEmail(email: String) {
        if (customerRepository.existsByEmail(email)) {
            throw RuntimeException("Email already exists: $email")
        }
    }

    fun existsById(id: UUID): Boolean {
        return customerRepository.existsById(id)
    }
}