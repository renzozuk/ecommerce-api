package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

interface OrderRepository : CrudRepository<Order, UUID> {
    fun findByCustomerId(customerId: UUID): List<Order>

    fun findByOrderDateBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Order>

    fun findByOrderDateBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus
    ): List<Order>

    fun countByStatus(status: OrderStatus): Long

    fun existsByCustomerId(customerId: UUID): Boolean
}
