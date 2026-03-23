package com.renzozukeram.ecommerce.services

import com.renzozukeram.ecommerce.exceptions.BusinessException
import com.renzozukeram.ecommerce.exceptions.ResourceNotFoundException
import com.renzozukeram.ecommerce.mappers.OrderItemMapper
import com.renzozukeram.ecommerce.model.dto.OrderItemRequest
import com.renzozukeram.ecommerce.model.dto.OrderItemResponse
import com.renzozukeram.ecommerce.model.dto.OrderItemUpdateRequest
import com.renzozukeram.ecommerce.model.entities.OrderStatus
import com.renzozukeram.ecommerce.repositories.OrderItemRepository
import com.renzozukeram.ecommerce.repositories.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class OrderItemService(
    private val orderItemRepository: OrderItemRepository,
    private val orderRepository: OrderRepository,
    private val orderItemMapper: OrderItemMapper
) {

    fun findByOrderId(orderId: UUID): List<OrderItemResponse> {
        if (!orderRepository.existsById(orderId)) {
            throw ResourceNotFoundException("Order", orderId)
        }
        return orderItemRepository.findByOrderId(orderId).map { orderItemMapper.toResponse(it) }
    }

    fun findById(id: UUID): OrderItemResponse {
        val item = orderItemRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("OrderItem", id) }
        return orderItemMapper.toResponse(item)
    }

    @Transactional
    fun addItem(orderId: UUID, request: OrderItemRequest): OrderItemResponse {
        val order = orderRepository.findByIdWithDetails(orderId)
            .orElseThrow { ResourceNotFoundException("Order", orderId) }

        if (order.status != OrderStatus.PENDING) {
            throw BusinessException("Cannot add items to an order with status ${order.status}")
        }

        val item = orderItemMapper.toEntity(request, order)
        order.items.add(item)
        order.totalAmount = order.items.sumOf { it.totalPrice }
        orderRepository.save(order)

        return orderItemMapper.toResponse(orderItemRepository.save(item))
    }

    @Transactional
    fun updateItem(id: UUID, request: OrderItemUpdateRequest): OrderItemResponse {
        val item = orderItemRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("OrderItem", id) }

        if (item.order.status != OrderStatus.PENDING) {
            throw BusinessException("Cannot update items in an order with status ${item.order.status}")
        }

        orderItemMapper.updateEntity(item, request)
        val saved = orderItemRepository.save(item)

        val order = item.order
        order.totalAmount = order.items.sumOf { it.totalPrice }
        orderRepository.save(order)

        return orderItemMapper.toResponse(saved)
    }

    @Transactional
    fun deleteItem(id: UUID) {
        val item = orderItemRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("OrderItem", id) }

        if (item.order.status != OrderStatus.PENDING) {
            throw BusinessException("Cannot remove items from an order with status ${item.order.status}")
        }

        val order = item.order
        order.items.remove(item)
        order.totalAmount = order.items.sumOf { it.totalPrice }
        orderRepository.save(order)

        orderItemRepository.deleteById(id)
    }
}