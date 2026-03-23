package com.seuprojeto.domain.service

import com.renzozukeram.ecommerce.mappers.OrderMapper
import com.renzozukeram.ecommerce.model.dto.response.OrderResponse
import com.renzozukeram.ecommerce.model.entities.Customer
import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import com.renzozukeram.ecommerce.repositories.OrderRepository
import com.renzozukeram.ecommerce.services.CustomerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val customerService: CustomerService,
    private val orderMapper: OrderMapper
) {

    fun findAll(): Iterable<OrderResponse> {
        return orderRepository.findAll().map { o -> orderMapper.toResponse(o) }
    }

    fun findById(id: UUID): Order {
        return orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found with id: $id") }
    }

    @Transactional
    fun create(order: Order, customerId: UUID): Order {
        val customerResponse = customerService.findById(customerId)
        val customer = Customer(customerResponse.id, customerResponse.name, customerResponse.email, customerResponse.phoneNumber, customerResponse.createdAt, customerResponse.updatedAt)
        order.customer = customer

        order.totalAmount = order.items.sumOf { it.totalPrice }

        val savedOrder = orderRepository.save(order)

        return savedOrder
    }

    @Transactional
    fun updateStatus(id: UUID, newStatus: OrderStatus): Order {
        val order = findById(id)

        validateStatusTransition(order.status, newStatus)

        order.status = newStatus
        return orderRepository.save(order)
    }

    private fun validateStatusTransition(current: OrderStatus, newStatus: OrderStatus) {
        val allowedTransitions = mapOf(
            OrderStatus.PENDING to setOf(OrderStatus.PROCESSING, OrderStatus.CANCELLED),
            OrderStatus.PROCESSING to setOf(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED to setOf(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED to emptySet(),
            OrderStatus.CANCELLED to emptySet()
        )

        if (newStatus !in (allowedTransitions[current] ?: emptySet())) {
            throw RuntimeException("Invalid status transition from $current to $newStatus")
        }
    }
}