package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Order
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface OrderRepository : CrudRepository<Order, UUID>
