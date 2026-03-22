package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Customer
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CustomerRepository : CrudRepository<Customer, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Customer?
}