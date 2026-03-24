package com.renzozukeram.ecommerce.model.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
open class Order (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: Customer,

    @Column(name = "order_date", nullable = false)
    var orderDate: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(name = "total_amount", nullable = false)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var items: MutableList<OrderItem> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)