package com.renzozukeram.ecommerce.services

import com.renzozukeram.ecommerce.exceptions.BusinessException
import com.renzozukeram.ecommerce.exceptions.ResourceNotFoundException
import com.renzozukeram.ecommerce.mappers.OrderMapper
import com.renzozukeram.ecommerce.model.dto.OrderRequest
import com.renzozukeram.ecommerce.model.dto.OrderResponse
import com.renzozukeram.ecommerce.model.dto.OrderUpdateRequest
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import com.renzozukeram.ecommerce.repositories.CustomerRepository
import com.renzozukeram.ecommerce.repositories.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val orderMapper: OrderMapper
) {

    fun findAll(pageable: Pageable): Page<OrderResponse> =
        orderRepository.findAll(pageable).map { orderMapper.toResponse(it) }

    fun findById(id: UUID): OrderResponse {
        val order = orderRepository.findByIdWithDetails(id)
            .orElseThrow { ResourceNotFoundException("Order", id) }
        return orderMapper.toResponse(order)
    }

    fun findByCustomerId(customerId: UUID, pageable: Pageable): Page<OrderResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw ResourceNotFoundException("Customer", customerId)
        }
        return orderRepository.findByCustomerId(customerId, pageable).map { orderMapper.toResponse(it) }
    }

    fun findByStatus(status: OrderStatus, pageable: Pageable): Page<OrderResponse> =
        orderRepository.findByStatus(status, pageable).map { orderMapper.toResponse(it) }

    @Transactional
    fun create(request: OrderRequest): OrderResponse {
        val customer = customerRepository.findById(request.customerId)
            .orElseThrow { ResourceNotFoundException("Customer", request.customerId) }

        val order = orderMapper.toEntity(request, customer)
        return orderMapper.toResponse(orderRepository.save(order))
    }

    @Transactional
    fun updateStatus(id: UUID, request: OrderUpdateRequest): OrderResponse {
        val order = orderRepository.findByIdWithDetails(id)
            .orElseThrow { ResourceNotFoundException("Order", id) }

        validateStatusTransition(order.status, request.status)
        order.status = request.status

        return orderMapper.toResponse(orderRepository.save(order))
    }

    @Transactional
    fun delete(id: UUID) {
        val order = orderRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Order", id) }

        if (order.status == OrderStatus.SHIPPED || order.status == OrderStatus.DELIVERED) {
            throw BusinessException("Cannot delete an order with status ${order.status}")
        }

        orderRepository.deleteById(id)
    }

    private fun validateStatusTransition(current: OrderStatus, next: OrderStatus) {
        val validTransitions = mapOf(
            OrderStatus.PENDING to setOf(OrderStatus.PROCESSING, OrderStatus.CANCELLED),
            OrderStatus.PROCESSING to setOf(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED to setOf(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED to emptySet(),
            OrderStatus.CANCELLED to emptySet()
        )

        val allowed = validTransitions[current] ?: emptySet()
        if (next !in allowed) {
            throw BusinessException("Invalid status transition from $current to $next")
        }
    }
}