package com.seuprojeto.domain.service

import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import com.renzozukeram.ecommerce.repositories.OrderRepository
import com.renzozukeram.ecommerce.services.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val customerService: CustomerService
) {

    fun findAll(): Iterable<Order> {
        return orderRepository.findAll()
    }

    fun findById(id: UUID): Order {
        return orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found with id: $id") }
    }

    @Transactional
    fun create(order: Order, customerId: UUID): Order {
        val customer = customerService.findById(customerId)
        order.customer = customer

        order.totalAmount = order.items.sumOf { it.totalPrice }

        val savedOrder = orderRepository.save(order)

        customer.orders.add(savedOrder)

        return savedOrder
    }

    @Transactional
    fun updateStatus(id: UUID, newStatus: OrderStatus): Order {
        val order = findById(id)

        validateStatusTransition(order.status, newStatus)

        order.status = newStatus
        return orderRepository.save(order)
    }

    @Transactional
    fun cancelOrder(id: UUID): Order {
        val order = findById(id)

        if (order.status == OrderStatus.SHIPPED || order.status == OrderStatus.DELIVERED) {
            throw RuntimeException("Cannot cancel order that is already shipped or delivered")
        }

        order.status = OrderStatus.CANCELLED
        return orderRepository.save(order)
    }

    fun findOrdersByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Order> {
        return orderRepository.findByOrderDateBetween(startDate, endDate, pageable)
    }

    fun calculateTotalRevenue(startDate: LocalDateTime, endDate: LocalDateTime): BigDecimal {
        val orders = orderRepository.findByOrderDateBetweenAndStatus(
            startDate,
            endDate,
            OrderStatus.DELIVERED
        )
        return orders.sumOf { it.totalAmount }
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