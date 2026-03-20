package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.OrderItem
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderItemRepository : CrudRepository<OrderItem, UUID>