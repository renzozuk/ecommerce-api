package com.renzozukeram.ecommerce.services

import com.renzozukeram.ecommerce.exceptions.DuplicateResourceException
import com.renzozukeram.ecommerce.exceptions.ResourceNotFoundException
import com.renzozukeram.ecommerce.mappers.CustomerMapper
import com.renzozukeram.ecommerce.model.dto.CustomerRequest
import com.renzozukeram.ecommerce.model.dto.CustomerResponse
import com.renzozukeram.ecommerce.model.dto.CustomerUpdateRequest
import com.renzozukeram.ecommerce.model.dto.CustomerWithOrdersResponse
import com.renzozukeram.ecommerce.repositories.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerMapper: CustomerMapper
) {

    fun findAll(pageable: Pageable): Page<CustomerResponse> =
        customerRepository.findAll(pageable).map { customerMapper.toResponse(it) }

    fun findById(id: UUID): CustomerResponse {
        val customer = customerRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Customer", id) }
        return customerMapper.toResponse(customer)
    }

    fun findByIdWithOrders(id: UUID): CustomerWithOrdersResponse {
        val customer = customerRepository.findByIdWithOrders(id)
            .orElseThrow { ResourceNotFoundException("Customer", id) }
        return customerMapper.toResponseWithOrders(customer)
    }

    @Transactional
    fun create(request: CustomerRequest): CustomerResponse {
        if (customerRepository.existsByEmail(request.email)) {
            throw DuplicateResourceException("Customer with email '${request.email}' already exists")
        }
        val customer = customerMapper.toEntity(request)
        return customerMapper.toResponse(customerRepository.save(customer))
    }

    @Transactional
    fun update(id: UUID, request: CustomerUpdateRequest): CustomerResponse {
        val customer = customerRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Customer", id) }

        if (customerRepository.existsByEmailAndIdNot(request.email, id)) {
            throw DuplicateResourceException("Customer with email '${request.email}' already exists")
        }

        customerMapper.updateEntity(customer, request)
        return customerMapper.toResponse(customerRepository.save(customer))
    }

    @Transactional
    fun delete(id: UUID) {
        if (!customerRepository.existsById(id)) {
            throw ResourceNotFoundException("Customer", id)
        }
        customerRepository.deleteById(id)
    }
}