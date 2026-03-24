package com.renzozukeram.ecommerce.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.mutableListOf


@Entity
@Table(name = "customers")
open class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "phone_number")
    var phoneNumber: String?,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var orders: MutableList<Order> = mutableListOf()
)