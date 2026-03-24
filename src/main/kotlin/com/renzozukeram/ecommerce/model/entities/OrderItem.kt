package com.renzozukeram.ecommerce.model.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_items")
open class OrderItem (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @Column(nullable = false)
    var productName: String,

    @Column(nullable = false)
    var quantity: Int,

    @Column(name = "unit_price", nullable = false)
    var unitPrice: BigDecimal,

    @Column(name = "total_price", nullable = false)
    var totalPrice: BigDecimal
)