package com.renzozukeram.ecommerce.mappers

import com.renzozukeram.ecommerce.model.dto.OrderItemRequest
import com.renzozukeram.ecommerce.model.dto.OrderItemResponse
import com.renzozukeram.ecommerce.model.dto.OrderItemUpdateRequest
import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderItem
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderItemMapper {

    fun toEntity(request: OrderItemRequest, order: Order): OrderItem {
        val totalPrice = request.unitPrice.multiply(BigDecimal(request.quantity))
        return OrderItem(
            order = order,
            productName = request.productName,
            quantity = request.quantity,
            unitPrice = request.unitPrice,
            totalPrice = totalPrice
        )
    }

    fun toResponse(orderItem: OrderItem): OrderItemResponse =
        OrderItemResponse(
            id = orderItem.id!!,
            productName = orderItem.productName,
            quantity = orderItem.quantity,
            unitPrice = orderItem.unitPrice,
            totalPrice = orderItem.totalPrice
        )

    fun updateEntity(orderItem: OrderItem, request: OrderItemUpdateRequest): OrderItem {
        val totalPrice = request.unitPrice.multiply(java.math.BigDecimal(request.quantity))
        orderItem.productName = request.productName
        orderItem.quantity = request.quantity
        orderItem.unitPrice = request.unitPrice
        orderItem.totalPrice = totalPrice
        return orderItem
    }
}