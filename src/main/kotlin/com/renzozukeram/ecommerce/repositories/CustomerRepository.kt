package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<Customer, UUID> {

    fun existsByEmail(email: String): Boolean

    fun existsByEmailAndIdNot(email: String, id: UUID): Boolean

    fun findByEmail(email: String): Optional<Customer>

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.id = :id")
    fun findByIdWithOrders(id: UUID): Optional<Customer>
}