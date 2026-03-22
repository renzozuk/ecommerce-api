package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.UUID

interface OrderRepository : CrudRepository<Order, UUID> {
    fun findByCustomerId(customerId: UUID, pageable: Pageable): Page<Order>

    fun findByOrderDateBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<Order>

    fun findByOrderDateBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus
    ): List<Order>

    fun countByStatus(status: OrderStatus): Long

    fun existsByCustomerId(customerId: UUID): Boolean
}
