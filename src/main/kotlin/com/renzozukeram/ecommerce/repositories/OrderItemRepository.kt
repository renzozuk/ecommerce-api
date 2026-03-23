package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, UUID> {

    fun findByOrderId(orderId: UUID): List<OrderItem>

    fun deleteByOrderId(orderId: UUID)
}