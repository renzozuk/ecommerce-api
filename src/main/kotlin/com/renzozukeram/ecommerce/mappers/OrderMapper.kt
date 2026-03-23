package com.renzozukeram.ecommerce.mappers

import com.renzozukeram.ecommerce.model.dto.request.OrderRequest
import com.renzozukeram.ecommerce.model.dto.response.CustomerSummaryResponse
import com.renzozukeram.ecommerce.model.dto.response.OrderItemResponse
import com.renzozukeram.ecommerce.model.dto.response.OrderResponse
import com.renzozukeram.ecommerce.model.entities.Customer
import com.renzozukeram.ecommerce.model.entities.Order
import com.renzozukeram.ecommerce.model.entities.OrderItem
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderMapper {

    fun toEntity(request: OrderRequest, customer: Customer): Order {
        val order = Order(customer = customer)
        order.items = request.items.map { req ->
            OrderItem(
                order = order,
                productName = req.productName,
                quantity = req.quantity,
                unitPrice = req.unitPrice,
                totalPrice = req.unitPrice.multiply(BigDecimal.valueOf(req.quantity.toLong()))
            )
        }.toMutableList()
        order.totalAmount = order.items.sumOf { it.totalPrice }
        return order
    }

    fun toResponse(order: Order): OrderResponse {
        return OrderResponse(
            id = order.id!!,
            orderDate = order.orderDate,
            status = order.status,
            totalAmount = order.totalAmount,
            items = order.items.map { toItemResponse(it) },
            customerSummary = toCustomerSummary(order.customer)
        )
    }

    private fun toItemResponse(item: OrderItem): OrderItemResponse {
        return OrderItemResponse(
            productName = item.productName,
            quantity = item.quantity,
            unitPrice = item.unitPrice,
            totalPrice = item.totalPrice
        )
    }

    private fun toCustomerSummary(customer: Customer): CustomerSummaryResponse {
        return CustomerSummaryResponse(
            id = customer.id!!,
            name = customer.name,
            email = customer.email
        )
    }
}
