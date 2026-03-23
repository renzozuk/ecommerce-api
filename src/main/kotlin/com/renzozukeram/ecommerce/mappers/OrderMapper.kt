package com.renzozukeram.ecommerce.mappers

import com.renzozukeram.ecommerce.model.dto.OrderRequest
import com.renzozukeram.ecommerce.model.dto.OrderResponse
import com.renzozukeram.ecommerce.model.dto.OrderSummaryResponse
import com.renzozukeram.ecommerce.model.entities.Customer
import com.renzozukeram.ecommerce.model.entities.Order
import org.springframework.stereotype.Component

@Component
class OrderMapper(private val orderItemMapper: OrderItemMapper) {

    fun toEntity(request: OrderRequest, customer: Customer): Order {
        val order = Order(customer = customer)
        val items = request.items.map { itemRequest ->
            orderItemMapper.toEntity(itemRequest, order)
        }.toMutableList()
        order.items = items
        order.totalAmount = items.sumOf { it.totalPrice }
        return order
    }

    fun toResponse(order: Order): OrderResponse =
        OrderResponse(
            id = order.id!!,
            customerId = order.customer.id!!,
            customerName = order.customer.name,
            orderDate = order.orderDate,
            status = order.status,
            totalAmount = order.totalAmount,
            items = order.items.map { orderItemMapper.toResponse(it) },
            createdAt = order.createdAt
        )

    fun toSummaryResponse(order: Order): OrderSummaryResponse =
        OrderSummaryResponse(
            id = order.id!!,
            orderDate = order.orderDate,
            status = order.status,
            totalAmount = order.totalAmount,
            createdAt = order.createdAt
        )
}
