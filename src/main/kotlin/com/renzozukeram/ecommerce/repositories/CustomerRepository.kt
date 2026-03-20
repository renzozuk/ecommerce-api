package com.renzozukeram.ecommerce.repositories

import com.renzozukeram.ecommerce.model.entities.Customer
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerRepository : CrudRepository<Customer, UUID>